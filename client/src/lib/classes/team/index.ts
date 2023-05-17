import type {AthleteData} from "$lib/classes/user/athlete";
import type {TeamFinance} from "./teamFinance";


export interface TeamDTO {
    id: string,
    name: string,
    description: string,
    coachName?: string,
    coachId?: string,
    athletes?: AthleteData[],
    teamLogo: string
    numAthletes?: number
    teamFinance: TeamFinance
}

export class Team {

    static createFrom(teamDto: TeamDTO) {
        const team = new Team()
        console.log('teamDto', teamDto)
        team.id = teamDto.id
        team.name = teamDto.name
        team.description = teamDto.description
        team.coachId = teamDto.coachId ? teamDto.coachId : ''
        team.coachName = teamDto.coachName ? teamDto.coachName : ''
        team.athletes = teamDto.athletes ? [...teamDto.athletes] : []
        team.teamLogo = teamDto.teamLogo
        team.numAthletes = teamDto.numAthletes ? teamDto.numAthletes : 0
        team.teamFinance = teamDto.teamFinance

        return team
    }

    id: string = ''
    name: string = ''
    description: string = ''
    coachId: string = ''
    coachName: string = ''
    athletes: AthleteData[] = []
    teamLogo: string = ''
    numAthletes: number = 0
    teamFinance: TeamFinance | null = null
}

export interface DisplayTeamDTO {
    id: string,
    name: string,
    description: string,
    coachName: string,
    coachId: string,
    numAthletes: number,
    teamLogo: string
}

export class DisplayTeam {
    id: string = ''
    name: string = ''
    description: string = ''
    coachName: string = ''
    coachId: string = ''
    numAthletes: number = 0
    teamLogo: string = ''

    static createFrom(data: DisplayTeamDTO) {
        const team = new DisplayTeam()

        team.id = data.id
        team.name = data.name
        team.description = data.description
        team.coachId = data.coachId
        team.coachName = data.coachName
        team.numAthletes = data.numAthletes
        team.teamLogo = data.teamLogo

        return team
    }
}