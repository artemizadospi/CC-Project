package com.db.gourmetguide.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/health")
public class HealthController {
    @GetMapping
    public String healthCheck (){
        return "ok";
    }
}

