#include <stdio.h>
#define STRING_SIZE 100

struct ExamDay {
    int day, year;
    char month[STRING_SIZE];
};

struct Student {
    char name[STRING_SIZE], surname[STRING_SIZE];
    int groupNumber;
    struct ExamDay examDay;
};

int main(void)
{
    struct Student student;
    scanf("%s", student.name);
    scanf("%s", student.surname);
    scanf("%s", student.examDay.month);
    scanf("%d%d%d", &student.groupNumber, &student.examDay.day, &student.examDay.year);

    printf("name = %s\n", student.name);
    printf("surname = %s\n", student.surname);
    printf("groupNumber = %d\nexamDay = %d\nexamYear = %d\n", student.groupNumber, student.examDay.day, student.examDay.year);
    printf("examMonth = %s", student.examDay.month);

    return 0;
}
