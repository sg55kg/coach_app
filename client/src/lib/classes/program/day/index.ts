import { Exercise, type ExerciseDTO } from '$lib/classes/program/exercise';
import type { Dayjs } from 'dayjs';
import dayjs from 'dayjs';

export interface IDay {
    id?: string;
    date: Dayjs;
    exercises: Exercise[];
    isRestDay: boolean;
    warmUp: WarmUp | null;
}

export interface DayDTO {
    id: string;
    date: string;
    exercises: ExerciseDTO[];
    isRestDay: boolean;
    warmUp: WarmUp | null;
}

export class Day implements IDay {
    static createFrom = (dayDTO: DayDTO) => {
        let day = new Day();

        day.id = dayDTO.id;
        day.date = dayjs(dayDTO.date);
        day.exercises = dayDTO.exercises
            .map(e => Exercise.createFrom(e))
            .sort((a, b) => a.order - b.order);
        day.isRestDay = dayDTO.isRestDay;
        day.warmUp = dayDTO.warmUp;

        return day;
    };

    copy() {
        let copy = Day.createFrom(JSON.parse(JSON.stringify(this)));
        copy.id = '';
        copy.warmUp = copy.warmUp ? { ...copy.warmUp, id: '' } : copy.warmUp;
        copy.exercises = copy.exercises.map(e => e.copy());

        return copy;
    }

    id: string = '';
    date: Dayjs = dayjs();
    exercises: Exercise[] = [];
    isRestDay: boolean = false;
    warmUp: WarmUp | null = null;
}

export class WarmUp {
    id: string = '';
    instructions: string = '';
}
