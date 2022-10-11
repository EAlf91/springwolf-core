package io.github.stavshamir.springwolf.asyncapi.scanners.channels;

import com.asyncapi.v2.binding.sqs.SQSOperationBinding;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SQSCustomOperationBinding extends SQSOperationBinding {

    @NonNull
    String opid;
}
