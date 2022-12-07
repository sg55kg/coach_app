export interface UserDTO {
    id: string,
    athleteData: any,
    coachData: any,
    createdAt: string,
    email: string,
    roles: any[],
    updatedAt: string,
    username: string
}

export class User {

    static build(userDTO: UserDTO) {
        const user = new User()

        user.athleteData = userDTO.athleteData;
        user.coachData = userDTO.coachData;
        user.createdAt = new Date(userDTO.createdAt);
        user.updatedAt = new Date(userDTO.updatedAt);
        user.email = userDTO.email;
        user.id = userDTO.id;
        user.username = userDTO.username;
        user.roles = userDTO.roles;

        return user;
    }

    athleteData: any = null;
    coachData: any = null;
    createdAt: Date = new Date();
    email: string = '';
    id: string = '';
    roles: any[] = [];
    updatedAt: Date = new Date();
    username: string = '';
}