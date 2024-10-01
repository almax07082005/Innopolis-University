#include <stdbool.h>
#include <stdio.h>
#include <signal.h>
#include <unistd.h>

void handler(const int sig) {
    if (sig == SIGINT) {
        printf("SIGINT received\n");
        raise(SIGINT);
    } else if (sig == SIGTERM) {
        printf("SIGTERM received\n");
    }
}

int main(void) {

    signal(SIGINT, handler);
    signal(SIGTERM, handler);

    while (true) {
        printf("Hello, World!\n");
        sleep(2);
    }

    return 0;
}
