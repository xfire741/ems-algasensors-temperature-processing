package com.algaworks.algasensors.temperature_processing.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.algaworks.algasensors.temperature_processing.api.model.TemperatureLogOutput;
import com.algaworks.algasensors.temperature_processing.common.IdGenerator;

import io.hypersistence.tsid.TSID;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/sensors/{sensorId}/temperatures/data")
@Slf4j
public class TemperatureProcessingController {

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void data(@PathVariable("sensorId") TSID sensorId, @RequestBody String input) {
        if (input == null || input.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Double temperature;

        try {
            temperature = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid temperature value: " + input, e);
        }

        TemperatureLogOutput temperatureLog = TemperatureLogOutput.builder()
                .id(IdGenerator.generateTimeBasedUUID())
                .sensorId(sensorId)
                .value(temperature)
                .registeredAt(java.time.OffsetDateTime.now())
                .build();

        log.info(temperatureLog.toString());

    }

}
