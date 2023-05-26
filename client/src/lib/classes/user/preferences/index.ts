export interface UserPreferencesDTO {
    id: string;
    weight: 'kg' | 'lb';
    mode: 'light' | 'dark';
}

export class UserPreferences {
    public static createFrom(data: UserPreferencesDTO) {
        const up = new UserPreferences();

        up.mode = data.mode;
        up.weight = data.weight;
        up.id = data.id;

        return up;
    }
    id: string = '';
    weight: 'kg' | 'lb' = 'kg';
    mode: 'light' | 'dark' = 'dark';
}
