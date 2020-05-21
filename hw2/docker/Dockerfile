FROM alpine:3.11.5
RUN apk add --no-cache iperf3
CMD iperf3 -s -p 5432 -1 >/dev/null
EXPOSE 5432/tcp


























