def minorSum(leftUpperCornerI, leftUpperCornerJ, sideLength, field):
    sum = 0
    i = leftUpperCornerI
    while i - leftUpperCornerI < sideLength:
        j = leftUpperCornerJ
        while j - leftUpperCornerJ < sideLength:
            sum += field[i][j]
            j += 1
        i += 1
    return sum


def main():
    rows, columns = map(int, input().split())
    field = [list(map(int, input().split())) for i in range(rows)]
    rank = 0

    for i in range(rows):
        for j in range(columns):
            if not field[i][j]:
                continue
            if not rank:
                rank += 1

            sum = 1
            k = 2

            while k + i <= rows and k + j <= columns:
                sum = minorSum(i, j, k, field)
                if sum != k * k:
                    break
                if rank < k:
                    rank = k
                k += 1

    print(rank)


if __name__ == '__main__':
    main()
