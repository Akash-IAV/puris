/*
 * Copyright (c) 2024 Volkswagen AG
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package org.eclipse.tractusx.puris.backend.erpadapter.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.eclipse.tractusx.puris.backend.common.edc.domain.model.AssetType;
import org.eclipse.tractusx.puris.backend.common.util.PatternStore;
import org.eclipse.tractusx.puris.backend.stock.logic.dto.itemstocksamm.DirectionCharacteristic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErpAdapterRequest {

    public final static List<AssetType> SUPPORTED_TYPES = List.of(AssetType.ITEM_STOCK_SUBMODEL);

    @GeneratedValue
    @Id
    private UUID id;

    @Pattern(regexp = PatternStore.BPNL_STRING)
    @NotNull
    private String partnerBpnl;

    @ValidRequestType
    private AssetType requestType;

    @Pattern(regexp = PatternStore.NON_EMPTY_NON_VERTICAL_WHITESPACE_STRING)
    @NotNull
    private String sammVersion;

    @NotNull
    private Date requestDate;

    private Integer responseCode;

    private Date responseReceivedDate;

    @Pattern(regexp = PatternStore.NON_EMPTY_NON_VERTICAL_WHITESPACE_STRING)
    @NotNull
    private String ownMaterialNumber;

    private DirectionCharacteristic directionCharacteristic;

    // AssetType validation helpers:
    @Constraint(validatedBy = RequestTypeValidator.class)
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    private @interface ValidRequestType {
        String message() default
            "Request Type must be listed in SUPPORTED_TYPES";

        Class<?>[] groups() default {};

        Class<? extends Payload>[] payload() default {};
    }

    public static class RequestTypeValidator implements ConstraintValidator<ValidRequestType, AssetType> {
        @Override
        public boolean isValid(AssetType value, ConstraintValidatorContext context) {
            return value != null && SUPPORTED_TYPES.contains(value);
        }
    }
}
