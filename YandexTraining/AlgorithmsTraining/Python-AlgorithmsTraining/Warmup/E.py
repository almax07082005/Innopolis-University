def main():
    n, a, s = int(input()), list(map(int, input().split())), 0

    for i in range(n):
        s += a[i]
    s -= n * a[0]
    print(s, end='')

    s = s + n * a[0] - 2 * a[0] - a[1] * (n - 2)
    print('', s, end='')

    for i in range(2, n):
        s = s - 2 * a[i - 1] - a[i] * (n - 2 * i) + a[i - 1] * (n - 2 * (i - 1))
        print('', s, end='')


if __name__ == '__main__':
    main()
