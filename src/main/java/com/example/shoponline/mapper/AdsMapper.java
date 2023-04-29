package com.example.shoponline.mapper;

import com.example.shoponline.dto.AdsDTO;
import com.example.shoponline.dto.CreateAds;
import com.example.shoponline.dto.FullAds;
import com.example.shoponline.model.Ads;

public class AdsMapper {
    public static AdsDTO toDTO(Ads ads) {
        AdsDTO adsDTO = new AdsDTO();
        adsDTO.setPk(ads.getPk());
        adsDTO.setAuthor(ads.getAuthor().getId());
        adsDTO.setTitle(ads.getTitle());
        adsDTO.setPrice(ads.getPrice());
        adsDTO.setImage("/ads/image/" + ads.getPk());
        return adsDTO;
    }

    public static FullAds toFullAds(Ads ads) {
        FullAds fullAds = new FullAds();
        fullAds.setPk(ads.getPk());
        fullAds.setTitle(ads.getTitle());
        fullAds.setDescription(ads.getDescription());
        fullAds.setPrice(ads.getPrice());
        fullAds.setImage("/ads/image/" + ads.getPk());
        fullAds.setEmail(ads.getAuthor().getUsername());
        fullAds.setPhone(ads.getAuthor().getPhone());
        return fullAds;
    }

    public static Ads fromCreateAds(CreateAds createAds) {
        Ads ads = new Ads();
        ads.setTitle(createAds.getTitle());
        ads.setDescription(createAds.getDescription());
        ads.setPrice(createAds.getPrice());
        return ads;
    }
}
