package com.core.Parameterization.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UpdatedServiceRequest {

        private List<Long> oldServiceId;
        private List<Long> newServiceId;

}
