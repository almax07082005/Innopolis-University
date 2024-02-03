#include <stdio.h>

enum Weekdays {
    Mon = 1,
    Tue = 2,
    Wed = 3,
    Thu = 4,
    Fri = 5,
    Sat = 6,
    Sun = 7
};

int main(void)
{
    enum Weekdays weekdays;
    scanf("%d", &weekdays);
    switch (weekdays) {
//      ...
    }
    return 0;
}
