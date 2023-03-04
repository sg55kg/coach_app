import dayjs, {Dayjs} from "dayjs";

export interface AthleteRecordDTO {
    id: string,
    createdAt: string,
    lastUpdated: string,
    snatch: number
    clean_and_jerk: number
    jerk: number
    clean: number
    back_squat: number
    front_squat: number
    deadlift: number
    snatch_deadlift: number
    push_press: number
    strict_press: number
    bent_over_row: number
    power_snatch: number
    power_clean: number
    power_jerk: number
    hang_snatch: number
    hang_power_snatch: number
    hang_power_clean: number
    block_snatch: number
    block_clean: number
    block_power_snatch: number
    block_power_clean: number
    clean_pull: number
    snatch_pull: number
    snatch_high_pull: number
    bench_press: number
    pendlay_rows: number
    snatch_push_press: number
    overhead_squat: number
    squat_jerk: number
    max_pull_ups: number
    weighted_pull_up: number
    max_chin_ups: number
    weighted_chin_up: number
    pause_snatch: number
    pause_clean: number
    sandbag_carry_meters: number
    sandbag_carry_weight: number
    farmer_carry_meters: number
    farmer_carry_weight: number
    block_deadlift: number
    deficit_clean: number
    deficit_snatch: number
    deficit_deadlift: number
    romanian_deadlift: number
    snatch_romanian_deadlift: number
    hang_muscle_snatch: number
    muscle_snatch: number
    pause_jerk_dip: number
    pause_jerk_catch: number
    pause_deadlift: number
    sumo_deadlift: number
    low_hang_snatch: number
    high_hang_snatch: number
    low_hang_clean: number
    high_hang_clean: number
}

export class AthleteRecord {

    public id: string = ''
    public records: Map<string, number> = new Map<string, number>()
    public createdAt: Dayjs = dayjs()
    public lastUpdated: Dayjs = dayjs()

    constructor(data: AthleteRecordDTO) {
        for (const [key, value] of Object.entries(data)) {
            if (key.toLowerCase().includes('athlete-stats')) {
                // potential temporary fix until I find out why JsonIgnore doesn't work here
                continue
            }
            if (key === 'createdAt') {
                this.createdAt = dayjs(value)
            } else if (key === 'lastUpdated') {
                this.lastUpdated = dayjs(value);
            } else if (key === 'id') {
                this.id = value;
            } else {
                this.records.set(key.toLowerCase(), value)
            }
        }
    }
}

export const athleteRecordFields = [
    'snatch',
    'clean and jerk',
    'jerk',
    'clean',
    'back squat',
    'front squat',
    'deadlift',
    'snatch deadlift',
    'push press',
    'strict press',
    'bent over row',
    'power snatch',
    'power clean',
    'power jerk',
    'hang snatch',
    'hang power snatch',
    'hang power clean',
    'block snatch',
    'block clean',
    'block power snatch',
    'block power clean',
    'clean pull',
    'snatch pull',
    'snatch high pull',
    'bench press',
    'pendlay rows',
    'snatch push press',
    'overhead squat',
    'squat jerk',
    'max pull ups',
    'weighted pull up',
    'max chin ups',
    'weighted chin up',
    'pause snatch',
    'pause clean',
    'sandbag carry meters',
    'sandbag carry weight',
    'farmer carry meters',
    'farmer carry weight',
    'block deadlift',
    'deficit clean',
    'deficit snatch',
    'deficit deadlift',
    'romanian deadlift',
    'snatch romanian deadlift',
    'hang muscle snatch',
    'muscle snatch',
    'pause jerk dip',
    'pause jerk catch',
    'pause deadlift',
    'sumo deadlift',
    'low hang snatch',
    'high hang snatch',
    'low hang clean',
    'high hang clean',
]