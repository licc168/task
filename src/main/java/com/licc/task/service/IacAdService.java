package com.licc.task.service;//package com.licc.task.service;

import com.jumore.dove.dao.CommonDao;
import com.jumore.dove.dao.ParamMap;
import com.licc.task.domain.IacAd;
import com.licc.task.enums.EIacAdStatus;
import com.licc.task.vo.IacAdVo;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class IacAdService {
    @Resource
    CommonDao commonDao;

    public List<IacAdVo> listByStatus() {
        ParamMap paramMap = new ParamMap();
        paramMap.put("type", 0);
        return commonDao.listByParams(IacAdVo.class, "IacStartMapper.findList", paramMap);
    }

    public void updateStaus() {
      commonDao.execute("IacStartMapper.updateStatus",new ParamMap());
    }

    public void updateStausById(Long id,Integer status) {
    IacAd iacAd = this.commonDao.get(IacAd.class,id);
    iacAd.setStatus(status);
    commonDao.update(iacAd);
  }
    public  void update(Long id ,Integer broeseNum,Integer praiseNum){
      IacAd iacAd = this.commonDao.get(IacAd.class,id);
      iacAd.setBrowseNum(broeseNum);
      iacAd.setPraiseNum(praiseNum);
      commonDao.update(iacAd);

    }

}
