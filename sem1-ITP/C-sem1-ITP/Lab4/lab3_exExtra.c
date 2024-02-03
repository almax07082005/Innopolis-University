#include <stdio.h>
#define NAME_SIZE 50
#define INGREDIENTS_AMOUNT 10
#define RECIPES_AMOUNT 1000

struct Recipe {
    char name[NAME_SIZE];
    char ingredients[INGREDIENTS_AMOUNT][NAME_SIZE];
    int ingredientsAmount;
};

int main(void)
{
    struct Recipe recipes[RECIPES_AMOUNT];
    return 0;
}
