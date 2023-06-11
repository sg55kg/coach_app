export class TeamFinance {
    id: string = '';
    coachId: string = '';
    teamId: string = '';
    stripeConnectId: string = '';
    stripeStatus: StripeStatus = StripeStatus.NEW;
    teamPrice: number = 0;
    athleteCap: number = -1;
    requestRequired: boolean = false;
    inviteOnly: boolean = false;
}

export enum StripeStatus {
    NEW = 'NEW',
    ONBOARDING = 'ONBOARDING',
    CREATED = 'CREATED',
}
