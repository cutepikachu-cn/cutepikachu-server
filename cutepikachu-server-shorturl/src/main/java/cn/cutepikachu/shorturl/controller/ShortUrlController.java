package cn.cutepikachu.shorturl.controller;

import cn.cutepikachu.common.response.ResponseEntity;
import cn.cutepikachu.common.util.ResponseUtils;
import cn.cutepikachu.shorturl.model.dto.UrlMapCreateDTO;
import cn.cutepikachu.shorturl.service.IUrlMapService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-09-02 19:45-23
 */
@RestController
public class ShortUrlController {

    @Resource
    private IUrlMapService urlMapService;

    @GetMapping("/v1/{shortUrl}")
    public void getLongUrl(@PathVariable String shortUrl, HttpServletResponse response) throws IOException {
        String longUrl = urlMapService.getLongUrlByShortUrl(shortUrl);
        sendRedirect(longUrl, response);
    }

    @PostMapping("/v1/shorten")
    public ResponseEntity<String> createShortUrl(@RequestBody UrlMapCreateDTO urlMapCreateDTO) {
        String shortUrl = urlMapService.createUrlMap(urlMapCreateDTO.getLongUrl());
        return ResponseUtils.success("ok", shortUrl);
    }

    // 重定向响应
    public void sendRedirect(String longUrl, HttpServletResponse response) throws IOException {
        response.sendRedirect(longUrl);
        response.setHeader("Location", longUrl);
        response.setHeader("Connection", "close");
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }

}
