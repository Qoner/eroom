package com.eroom.web.service.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.eroom.web.constants.RentLifeConstants;
import com.eroom.web.constants.SystemConstants;
import com.eroom.web.entity.vo.rentlife.TaskInfoVo;
import org.springframework.stereotype.Service;

import com.eroom.web.constants.PaymentConstants;
import com.eroom.web.dao.pay.PayDetailDao;
import com.eroom.web.entity.po.TPayDetail;
import com.eroom.web.utils.exception.BusinessException;

@Service
public class PayDetailService {

	@Resource
	private PayDetailDao payDetailDao;

	/**
	 * 获取最新缴费信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TPayDetail> getLastPayDetail(Long custId) throws Exception {
	    if(custId == null || custId == 0){
	        throw new BusinessException("未获取到租客编号");
	    }
	    List<TPayDetail> list = payDetailDao.getLastTPayDetail(custId, PaymentConstants.LIMIT);
		return list;
	}

	/**
	 * 获取分页缴费情况
	 *
	 * @return
	 * @throws Exception
	 */
	public List<TPayDetail> getPayDetail(Long custId, int curPage) throws Exception {
		if(custId == null || custId == 0){
			throw new BusinessException("未获取到租客编号");
		}
		Map<String, Object> map = new HashMap<>();
		Long page = 0L;
		Long totle = payDetailDao.countTPayDetail(custId);
		if(totle != null){
			page = totle/ SystemConstants.LAST_DATA_LIMIT;
			if(totle%SystemConstants.LAST_DATA_LIMIT != 0){
				page += 1;
			}
		}
		List<TPayDetail> list = payDetailDao.getTPayDetail(custId, SystemConstants.LAST_DATA_LIMIT, curPage);
		map.put("list", list);
		map.put("totle", totle);
		map.put("page", page);
		map.put("curPage", curPage);
		return list;
	}

}
