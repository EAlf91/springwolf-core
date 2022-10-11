FROM amazoncorretto:17.0.3
MAINTAINER OV-G-V-IT-FORDERUNGEN@otto.de
VOLUME /tmp

RUN yum update -y

COPY build/libs/razorshark_dunning_core-0.1.jar /app/

USER nobody

ENTRYPOINT java -Dlog4j2.formatMsgNoLookups=true -jar /app/razorshark_dunning_core-0.1.jar -Xms512m -Xmx512m