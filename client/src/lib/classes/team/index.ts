

export interface TeamDTO {
    name: string,
    description: string,
}

export class Team {

    static createFrom(teamDto: TeamDTO) {
        const team = new Team()

        team.name = teamDto.name
        team.description = teamDto.description

        return team
    }

    name: string = ''
    description: string = ''
}