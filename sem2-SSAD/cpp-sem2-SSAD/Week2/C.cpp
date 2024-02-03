#include <iostream>

class Box {

private:
    unsigned length;
    unsigned width;
    unsigned height;

public:
    Box() = default;
    Box(Box& box) = default;
    Box(unsigned int length, unsigned int width, unsigned int height) : length(length), width(width), height(height) {}
    Box& operator=(const Box& box) {
        this->length = box.length;
        this->width = box.width;
        this->height = box.height;
    }

    unsigned getVolume() {
        return length * width * height;
    }

    void operator*(int scaleValue) {
        length *= scaleValue;
        width *= scaleValue;
        height *= scaleValue;
    }

    bool operator==(const Box& other) {
        return (length == other.length || length == other.height || length == other.width)
                && (width == other.length || width == other.width || width == other.height)
                && (height == other.length || height == other.width || height == other.height);
    }

    bool isBigger(Box other) {
        return getVolume() > other.getVolume();
    }

    bool isSmaller(Box other) {
        return getVolume() < other.getVolume();
    }
};

int main()
{
    return EXIT_SUCCESS;
}
