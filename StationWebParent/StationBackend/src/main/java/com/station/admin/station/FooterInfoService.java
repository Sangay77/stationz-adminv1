package com.station.admin.station;

import com.station.common.entity.FooterInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FooterInfoService {
    @Autowired
    private FooterInfoRepository repo;

    public FooterInfo getFooter() {
        return repo.findById(1L).orElse(new FooterInfo());
    }

    public FooterInfo save(FooterInfo footerInfo) {
        return repo.save(footerInfo);
    }

}

