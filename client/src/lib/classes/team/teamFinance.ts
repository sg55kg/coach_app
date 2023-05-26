export class TeamFinance {
    id: string = '';
    coachId: string = '';
    teamId: string = '';
    stripeConnectId: string = '';
    stripeStatus: StripeStatus = StripeStatus.NEW;
    teamPrice: number = 0;
}

export enum StripeStatus {
    NEW = 'NEW',
    ONBOARDING = 'ONBOARDING',
    CREATED = 'CREATED',
}
