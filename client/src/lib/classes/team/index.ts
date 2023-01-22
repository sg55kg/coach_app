import type {AthleteData} from "$lib/classes/user/athlete";


export interface TeamDTO {
    id: string,
    name: string,
    description: string,
    coachName?: string,
    coachId?: string,
    athletes?: AthleteData[],
    teamLogo: string
}

export class Team {

    static createFrom(teamDto: TeamDTO) {
        const team = new Team()

        team.id = teamDto.id
        team.name = teamDto.name
        team.description = teamDto.description
        team.coachId = teamDto.coachId ? teamDto.coachId : ''
        team.coachName = teamDto.coachName ? teamDto.coachName : ''
        team.athletes = teamDto.athletes ? [...teamDto.athletes] : []
        team.teamLogo = teamDto.teamLogo

        return team
    }

    id: string = ''
    name: string = ''
    description: string = ''
    coachId: string = ''
    coachName: string = ''
    athletes: AthleteData[] = []
    teamLogo: string = ''
}

export class DisplayTeam {
    id: string = ''
    name: string = ''
    description: string = ''
    coachName: string = ''
    coachId: string = ''
    numAthletes: number = 0
    teamLogo: string = ''
}