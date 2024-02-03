#include <stdio.h>

struct IP {
    short version: 4;
    short ihl: 4;
    short dscp: 6;
    short ecn: 2;
    short totalLength;
};

union Packet {
    int number;
    struct IP ip;
};

int main(void)
{
    union Packet packet;
    scanf("%d", &packet.number);

    printf("Version: %d\n", packet.ip.version);
    printf("IHL: %d\n", packet.ip.ihl);
    printf("DSCP: %d\n", packet.ip.dscp);
    printf("ECN: %d\n", packet.ip.ecn);
    printf("Total length: %d", packet.ip.totalLength);

    return 0;
}
