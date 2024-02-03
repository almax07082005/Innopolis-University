#include <iostream>

class Box {

private:
    unsigned length;
    unsigned width;
    unsigned height;

public:
    Box(): length(0), width(0), height(0) {}
    Box(Box& box) = default;
    Box(unsigned int length, unsigned int width, unsigned int height) : length(length), width(width), height(height) {}
    Box& operator=(const Box& box) {
        this->length = box.length;
        this->width = box.width;
        this->height = box.height;
    }
};

int main()
{
    return EXIT_SUCCESS;
}
