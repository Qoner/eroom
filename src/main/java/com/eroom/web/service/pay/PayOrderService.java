package com.eroom.web.service.pay;

import com.eroom.web.constants.PayConstants;
import com.eroom.web.constants.RoomConstants;
import com.eroom.web.constants.SystemConstants;
import com.eroom.web.dao.pay.PayOrderDao;
import com.eroom.web.dao.pay.RentOrderDao;
import com.eroom.web.entity.po.PayOrder;
import com.eroom.web.entity.po.RentOrder;
import com.eroom.web.service.BaseService;
import com.eroom.web.service.rent.RoomRentService;
import com.eroom.web.utils.exception.BusinessException;
import com.eroom.web.utils.util.DateUtil;
import com.eroom.web.utils.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PayOrderService extends BaseService {

    @Resource
    private PayOrderDao payOrderDao;

    @Resource
    private RentOrderDao rentOrderDao;

    @Resource
    private RoomRentService roomRentService;

    /**
     * 支付租住订单、订单生成定时任务定时执行
     * @param custRenterId
     * @param payOrderId
     * @throws Exception
     */
    public void payRentOrder(Long custRenterId, Long payOrderId) throws Exception {
        PayOrder payOrder = payOrderDao.get(PayOrder.class, payOrderId);
        //判断是否为此用户的支付订单
        if(custRenterId != payOrder.getCustRenterId()){
            throw new BusinessException(SystemConstants.ExceptionMsg.PARAM_NULL_EXCEPTION_MSG);
        }
        //支付修改数据    type:alipay  wechat     pay_order_state
        payOrder.setPayOrderState(PayConstants.PayOrder.OrderState.FINISH);
        payOrder.setType(PayConstants.PayOrder.Type.ALIPAY);
        payOrderDao.update(payOrder);

        //修改租住订单数据   paidEndTime   payPhase  paidAmount
        RentOrder rentOrder = rentOrderDao.get(RentOrder.class, payOrder.getRentOrderId());
        rentOrder.setPaidEndTime(payOrder.getEndTime());
        //支付期数加一
        int payPhase = rentOrder.getPayPhase()+1;
        rentOrder.setPayPhase(payPhase);
        //添加支付的总金额
        BigDecimal paidAmount = rentOrder.getPaidAmount();
        paidAmount.add(payOrder.getAmount());
        rentOrder.setPaidAmount(paidAmount);
        //修改订单状态，如果为未支付则修改为履行中
        if(PayConstants.RentOrder.RentOrderState.WAIT_PAY.equals(rentOrder.getRentOrderState())){
            rentOrder.setRentOrderState(PayConstants.RentOrder.RentOrderState.PAID);
        }
        rentOrderDao.update(rentOrder);

        //下架房源信息
        roomRentService.updateRoomRentPaid(payOrder.getRentId());
    }

    /**
     * 获取用户支付订单
     * @param custId
     * @param rentOrderId
     * @throws Exception
     */
    public List<PayOrder> getPayRentOrder(Long custId, Long rentOrderId) throws Exception {

        List<PayOrder> list = payOrderDao.getWaitPayOrder(custId, rentOrderId);

        return list;
    }

    /**
     * 获取用户支付订单
     * @param custId
     * @throws Exception
     */
    public List<PayOrder> getPayRentOrder(Long custId) throws Exception {

        List<PayOrder> list = payOrderDao.getWaitPayOrder(custId);

        return list;
    }

    /**
     * 生成新一期的支付订单
     * @param rentOrderId
     * @return
     * @throws Exception
     */
    public PayOrder addPayRentOrder(Long rentOrderId) throws Exception {
        RentOrder rentOrder = rentOrderDao.get(RentOrder.class, rentOrderId);
        if(rentOrder == null || rentOrder.getRentOrderId() == 0){
            logger.info("rentOrder为空或者rentOrderId为0:"+rentOrder.toString());
            throw new BusinessException(SystemConstants.ExceptionMsg.PARAM_NULL_EXCEPTION_MSG);
        }
        //订单已经支付完成所有款项
        if(rentOrder.getPayPhase() >= rentOrder.getTotlePhase()){
            throw new BusinessException(SystemConstants.ExceptionMsg.ORDER_FINISH_EXCEPTION_CODE, SystemConstants.ExceptionMsg.ORDER_FINISH_EXCEPTION_MSG);
        }
        PayOrder payOrder = new PayOrder();
        //判断是否是生成第一期支付订单
        if(rentOrder.getPayPhase() > 0){
            payOrder.setLateAmount(rentOrder.getLateAmount());
            payOrder.setMortgageAmount(rentOrder.getMortgageAmount());
            BigDecimal totleAmount = rentOrder.getRentAmount().add(rentOrder.getMortgageAmount()).add(rentOrder.getLateAmount());
            payOrder.setAmount(totleAmount);
        }else{
            payOrder.setLateAmount(BigDecimal.ZERO);
            payOrder.setMortgageAmount(BigDecimal.ZERO);
            BigDecimal totleAmount = rentOrder.getRentAmount().add(payOrder.getLateAmount());
            payOrder.setAmount(totleAmount);
        }
        payOrder.setRentOrderId(rentOrder.getRentOrderId());
        payOrder.setStartTime(rentOrder.getStartTime());
        payOrder.setEndTime(getNextEndTime(rentOrder.getPaidEndTime(), rentOrder.getRentTimeType()));
        payOrder.setLength(DateUtil.getDaysBetween(DateUtil.getDateString(payOrder.getStartTime(), DateUtil.YYYYMMDD), DateUtil.getDateString(payOrder.getEndTime(), DateUtil.YYYYMMDD)));

        payOrder.setRentAmount(rentOrder.getRentAmount());
        payOrder.setBedroomId(rentOrder.getBedroomId());
        payOrder.setCustRenterId(rentOrder.getCustRenterId());
        payOrder.setRentId(rentOrder.getRentId());
        payOrder.setRoomId(rentOrder.getRoomId());
        payOrder.setCreateTime(DateUtil.getCurrentDate());
        payOrder.setPayOrderState(PayConstants.PayOrder.OrderState.WAITING);
        logger.info("RentOrderService.saveRentOrder  payOrder:"+payOrder.toString());
        payOrder = payOrderDao.save(payOrder);
        return payOrder;
    }

    /**
     * 获取订单列表
     *
     * @return
     * @throws Exception
     */
    public List<PayOrder> getPayOrderList(Long custRenterId, String orderState) throws Exception {
        if(StringUtil.isBlank(orderState)){
            throw new BusinessException("订单状态为空");
        }
        List<PayOrder> list = null;
        if(PayConstants.PayOrder.OrderState.WAITING.equals(orderState)){
            list = payOrderDao.getTPayOrderList(custRenterId, orderState, null);
        }else if(PayConstants.PayOrder.OrderState.FINISH.equals(orderState)){
            list = payOrderDao.getTPayOrderList(custRenterId, orderState, null);
        }else{
            list = payOrderDao.getTPayOrderList(custRenterId, orderState, PayConstants.PayOrder.ORDER_ALL_LIMIT);
        }
        return list;
    }

    private Date getNextEndTime(Date startTime, String rentTimeType){
        Date endTime = null;

        //判断租期类型设置到期时间
        if(RoomConstants.RoomRentSet.RentTimeType.RENT_YEAR.equals(rentTimeType)){
            endTime = DateUtil.getOffsetMonthsTime(DateUtil.getTimestamp(startTime), 1);
        }else if(RoomConstants.RoomRentSet.RentTimeType.RENT_MOUNTH.equals(rentTimeType)){
            endTime = DateUtil.getOffsetMonthsTime(DateUtil.getTimestamp(startTime), 1);
        }else if(RoomConstants.RoomRentSet.RentTimeType.RENT_WEEK.equals(rentTimeType)){
            endTime = DateUtil.getOffsetDaysTime(DateUtil.getTimestamp(startTime), 7);
        }else if(RoomConstants.RoomRentSet.RentTimeType.RENT_THREEDAY.equals(rentTimeType)){
            endTime = DateUtil.getOffsetDaysTime(DateUtil.getTimestamp(startTime), 3);
        }else{
            throw new BusinessException(SystemConstants.ExceptionMsg.SYS_ERROR_EXCEPTION_MSG);
        }

        return endTime;
    }
}
