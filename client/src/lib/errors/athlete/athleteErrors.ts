export class ExerciseMaxWeightError extends Error {
    constructor(message: string, id: string) {
        super();
        this.message = message;
        this.id = id;
    }
    id: string = '';
}

export class ExerciseMaxRepsError extends Error {
    constructor(message: string, id: string) {
        super();
        this.message = message;
        this.id = id;
    }
    id: string = '';
}

export class AthleteRecordError extends Error {
    constructor(message: string) {
        super();
        this.message = message;
    }
}
