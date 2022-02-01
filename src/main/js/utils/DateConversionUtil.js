export const convertSecondsToTime = (second) => {
    if(second < 0){
        return 0 + 's'
    }

    const d = Math.floor(second / (3600 * 24));
    second  -= d * 3600 * 24;
    const h = Math.floor(second / 3600);
    second  -= h * 3600;
    const m = Math.floor(second / 60);
    second  -= m * 60;
    const tmp = [];

    (d) && tmp.push(d + 'd');
    (d || h) && tmp.push(h + 'h');
    (d || h || m) && tmp.push(m + 'm');

    tmp.push(second + 's');

    return tmp.join(' ');
}