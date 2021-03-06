package com.eroom.web.dao.rent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eroom.web.service.rent.BookStateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.eroom.web.constants.RoomConstants;
import com.eroom.web.dao.BaseDao;
import com.eroom.web.entity.po.RoomBook;
import com.eroom.web.entity.vo.rent.RoomBookVo;

import javax.annotation.Resource;

@Repository
public class RoomBookDao extends BaseDao {

    @Resource
    private BookStateUtil stateUtil;

    /**
     * 获取租房信息表
     * 
     * @return RoomBook
     * @throws Exception
     * @author tendy
     */
    public List<RoomBook> getTRoomBook() throws Exception {
        String hql = "from RoomBook order by sortId desc";
        List<RoomBook> list = this.getList(hql);
        if (!CollectionUtils.isEmpty(list)) {
            return list;
        }
        return null;
    }

    /**
     * 通过custId获取TRoomBook
     * 
     * @return custId
     * @throws Exception
     * @author tendy
     */
    public RoomBook getTRoomBookApplied(Long custId, Long roomId, Date startTime, Date endTime)
            throws Exception {
        String hql = "from RoomBook where roomId = :roomId "
                + " and applyState in (:applyState1, :applyState2) "
                + " and ((startTime >= :startTime and startTime <= :endTime) or (endTime >= :startTime and endTime <= :endTime)) "
                + " order by startTime desc";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("roomId", roomId);
        params.put("applyState1", RoomConstants.RoomBook.ApplyState.AGREE);
        params.put("applyState2", RoomConstants.RoomBook.ApplyState.APPLYING);
        params.put("startTime", startTime);
        params.put("endTime", endTime);

        List<RoomBook> list = this.getList(hql, params);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 通过custId获取TRoomBook
     * 
     * @return custId
     * @throws Exception
     * @author tendy
     */
    public RoomBook getTRoomBook(Long custId) throws Exception {
        String hql = "from RoomBook where custRenterId = :custId order by startTime desc";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);

        List<RoomBook> list = this.getList(hql, params);
        if (!CollectionUtils.isEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 通过custId获取申请中的RoomBookVo
     * 
     * @return custId
     * @throws Exception
     * @author tendy
     */
    public List<RoomBookVo> getRoomBookVo(Long custId, List<String> applyStateList)
            throws Exception {
        StringBuffer hql = new StringBuffer(); 
        hql.append("select new com.eroom.web.entity.vo.rent.RoomBookVo(trb.bookId, trb.rentId, trb.roomId, trb.custOwnerId, trb.bedRoomId, ");
        hql.append("trb.custRenterId, tbi.imageUrl, tri.imageUrl, tri.name, ");
        hql.append("trr.price, tri.roomType, tbi.space, tbi.decorate, trb.applyState, trb.startTime, trb.endTime, trb.updateTime) ");
        hql.append(" from RoomBook trb, RoomRent trr, BedroomInfo tbi, RoomInfo tri ");
        hql.append(" where trb.custRenterId = :custId and trb.rentId = trr.rentId ");
        hql.append(" and trb.roomId = tri.roomId and trb.bedRoomId = tbi.bedroomId ");
        if (!CollectionUtils.isEmpty(applyStateList)) {
            hql.append(" and trb.applyState in (");
            for (int i = 0; i < applyStateList.size(); i++) {
                if (i == 0) {
                    hql.append(":applyState" + i);
                    continue;
                }
                hql.append(" ,:applyState" + i);
            }
            hql.append(") ");
        }
        hql.append(" order by trb.startTime desc");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);
        if (!CollectionUtils.isEmpty(applyStateList)) {
            for (int i = 0; i < applyStateList.size(); i++) {
                params.put("applyState" + i, applyStateList.get(i));
            }
        }
        List<RoomBookVo> list = this.getList(hql.toString(), params);
        if (!CollectionUtils.isEmpty(list)) {
            list = stateUtil.checkRoomBookVoState(list);
            return list;
        }
        return null;
    }
    
    /**
     * 通过custOwnerId获取申请中的RoomBookVo
     * 
     * @return custId
     * @throws Exception
     * @author tendy
     */
    public List<RoomBookVo> getFdRoomBookVo(Long custId, List<String> applyStateList)
            throws Exception {
        StringBuffer hql = new StringBuffer(); 
        hql.append("select new com.eroom.web.entity.vo.rent.RoomBookVo(trb.bookId, trb.rentId, trb.roomId, trb.custOwnerId, trb.bedRoomId, ");
        hql.append("trb.custRenterId, tbi.imageUrl, tri.imageUrl, tri.name, ");
        hql.append("trr.price, tri.roomType, tbi.space, tbi.decorate, trb.applyState, trb.startTime, trb.endTime, trb.updateTime) ");
        hql.append(" from RoomBook trb, RoomRent trr, BedroomInfo tbi, RoomInfo tri ");
        hql.append(" where trb.custOwnerId = :custId and trb.rentId = trr.rentId ");
        hql.append(" and trb.roomId = tri.roomId and trb.bedRoomId = tbi.bedroomId ");
        if (!CollectionUtils.isEmpty(applyStateList)) {
            hql.append(" and trb.applyState in (");
            for (int i = 0; i < applyStateList.size(); i++) {
                if (i == 0) {
                    hql.append(":applyState" + i);
                    continue;
                }
                hql.append(" ,:applyState" + i);
            }
            hql.append(") ");
        }
        hql.append(" order by trb.startTime desc");
        
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("custId", custId);
        if (!CollectionUtils.isEmpty(applyStateList)) {
            for (int i = 0; i < applyStateList.size(); i++) {
                params.put("applyState" + i, applyStateList.get(i));
            }
        }

        List<RoomBookVo> list = this.getList(hql.toString(), params);
        if (!CollectionUtils.isEmpty(list)) {
            list = stateUtil.checkRoomBookVoState(list);
            return list;
        }
        return null;
    }

    /**
     * 定时任务执行，预约过期修改状态
     *
     * @throws Exception
     * @author tendy
     */
    public int checkBooking(Date time) throws Exception {
        String hql = "update RoomBook set applyState = :timeOut where endTime <= :time and applyState = :applyState";

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("time", time);
        params.put("timeOut", RoomConstants.RoomBook.ApplyState.TIMEOUT);
        params.put("applyState", RoomConstants.RoomBook.ApplyState.AGREE);

        int result = this.update(hql, params);
        return result;
    }
}
