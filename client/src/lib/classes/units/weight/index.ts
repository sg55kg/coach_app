export class WeightUnit {
    constructor(value: number) {
        this.value = value;
    }
    value: number = 0;

    public getValue(units: 'kg' | 'lb') {
        if (units === 'kg') {
            return this.value;
        } else {
            return Math.round(this.value * 2.20462);
        }
    }

    public setValue(val: number, units: 'kg' | 'lb') {
        if (units === 'kg') {
            this.value = val;
        } else {
            this.value = Math.round(val / 2.20462);
        }
    }
}
