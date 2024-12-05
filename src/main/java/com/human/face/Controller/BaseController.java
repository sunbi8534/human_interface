package com.human.face.Controller;

import com.human.face.Dto.NewExperimentResponse;
import com.human.face.Dto.TargetRequest;
import com.human.face.Dto.UserMoveRequest;
import com.human.face.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class BaseController {
    Service service;
    public BaseController(Service service) {
        this.service = service;
    }
    @PostMapping("/exp/new")
    public ResponseEntity<NewExperimentResponse> getNewExperimentNum() {
        return ResponseEntity.ok(service.getNewExperimentNum());
    }

    @PostMapping("/exp/{id}/{type}/target/new")
    public void saveTarget(
            @PathVariable("id") int experimentId,
            @PathVariable("type") String type,
            @RequestBody TargetRequest targetRequest) {
        service.saveTarget(experimentId, type, targetRequest);
    }

    @PostMapping("/exp/{id}/{type}/target/{seq}/user-move")
    public void saveUserMove(
            @PathVariable("id") int experimentId,
            @PathVariable("type") String type,
            @PathVariable("seq") int sequence,
            @RequestBody UserMoveRequest userMoveRequest) {
        service.saveUserMove(experimentId, type, sequence, userMoveRequest);
    }

    @PostMapping("/export")
    public void export(
            @RequestParam int experimentId) {
        service.export(experimentId);
    }
}
