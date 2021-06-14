package com.atguigu.eduorder.client;

import com.atguigu.commonutils.UcenterMember;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    @GetMapping("/educenter/member/getUcenter/{memberId}")
    public UcenterMember getUcenterById(@PathVariable("memberId") String memberId);
}
