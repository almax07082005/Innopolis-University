#include <stdio.h>

struct Date {
    int day: 5;
    int month: 4;
    int year: 7;
};

int main(void)
{
    struct Date date;
    int day, month, year;
    scanf("%d%d%d", &day, &month, &year);
    date.day = day;
    date.month = month;
    date.year = year;
    printf("%d %d %d\n%llu", date.day, date.month, date.year, sizeof(date));
    return 0;
}
