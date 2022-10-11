package io.github.stavshamir.springwolf.asyncapi.scanners.channels;

import com.asyncapi.v2.binding.sqs.SQSChannelBinding;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SQSCustomChannelBinding extends SQSChannelBinding {

    @NonNull
    String id;


}
