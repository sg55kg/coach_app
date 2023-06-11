
export interface StripeConnectAccount {
    business_profile: StripeBusinessProfile,
    business_type: any,
    capabilities: StripeCapabilities,
    charges_enabled: boolean,
    company: any,
    controller: StripeController,
    country: string,
    created: Date,
    default_currency: string,
    deleted: any,
    details_submitted: boolean,
    email: string,
    external_accounts: StripeExternalAccount,
    future_requirements: StripeRequirement,
    id: string,
    individual: any,
    metadata: any,
    object: string,
    payouts_enabled: boolean,
    requirements: StripeRequirement,
    settings: StripeSettings,
    tos_acceptance: StripeTosAcceptance,
    type: string
}

export interface StripeBusinessProfile {
    ncc: string,
    name: string,
    product_description: string,
    support_address: StripeAddress,
    support_email: string,
    support_phone: string,
    support_url: string,
    url: string
}

export interface StripeAddress {
    city: string,
    country: string,
    line1: string,
    "line2": string,
    postal_code: string,
    state: string
}

export interface StripeCapabilities {
    acss_debit_payments: string,
    affirm_payments: string,
    afterpay_clearpay_payments: string,
    au_becs_debit_payments: any,
    bacs_debit_payments: any,
    bancontact_payments: string,
    bank_transfer_payments: any,
    blik_payments: any,
    boleto_payments: any,
    card_issuing: any,
    card_payments: string,
    cartes_bancaires_payments: string,
    eps_payments: string,
    fpx_payments: any,
    giropay_payments: string,
    grabpay_payments: any,
    ideal_payments: string,
    jcb_payments: any,
    klarna_payments: string,
    konbini_payments: any,
    legacy_payments: any,
    link_payments: string,
    oxxo_payments: any,
    p24_payments: string,
    paynow_payments: any,
    promptpay_payments: any,
    sepa_debit_payments: string,
    sofort_payments: string,
    tax_reporting_us_1099_k: any,
    tax_reporting_us_1099_misc: any,
    transfers: string,
    treasury: any,
    us_bank_account_ach_payments: string
}

export interface StripeController {
    is_controller: boolean,
    type: string
}

export interface StripeExternalAccount {
   data: StripeBankAccount[],
   has_more: boolean,
   object: string,
   request_params: any,
   url: string
}

export interface StripeBankAccount {
    account: string,
    account_holder_name: any,
    account_holder_type: any,
    account_type: any,
    available_payout_methods: string[],
    bank_name: string,
    country: string,
    currency: string,
    customer: any,
    default_for_currency: true,
    deleted: any,
    fingerprint: string,
    id: string,
    last4: string,
    metadata: any,
    object: string,
    routing_number: string,
    status: string
}

export interface StripeRequirement {
    alternatives: any[],
    current_deadline: any,
    currently_due: any[],
    disabled_reason: any,
    errors: any[],
    eventually_due: any[],
    past_due: any[],
    pending_verification: any[]
}

export interface StripeSettings {
    bacs_debit_payments: StripeBacsDebitPayments,
    branding: StripeBranding,
    card_issuing: StripeCardIssuing,
    card_payments: StripeCardPayments,
    dashboard: StripeDashboard,
    payments: StripePayments,
    payouts: StripePayouts,
    sepa_debit_payments: StripeSepaDebitPayments,
    treasury: any
}

export interface StripeBacsDebitPayments {
    display_name: string
}

export interface StripeBranding {
    icon: string,
    logo: string,
    primary_color: string,
    secondary_color: string
}

export interface StripeCardIssuing {
    tos_acceptance: StripeTosAcceptance
}

export interface StripeCardPayments {
    decline_on: StripeDeclineOn
    statement_descriptor_prefix: string,
    statement_descriptor_prefix_kana: string,
    statement_descriptor_prefix_kanji: string
}

export interface StripeDeclineOn {
    avs_failure: boolean,
    cvc_failure: boolean
}

export interface StripeDashboard {
    display_name: string,
    timezone: string
}

export interface StripePayments {
    statement_descriptor: string,
    statement_descriptor_kana: string,
    statement_descriptor_kanji: string,
    statement_descriptor_prefix_kana: string,
    statement_descriptor_prefix_kanji: string
}

export interface StripePayouts {
    debit_negative_balances: boolean,
    schedule: StripePayoutSchedule,
    statement_descriptor: boolean
}

export interface StripePayoutSchedule {
    delay_days: number,
    interval: string,
    monthly_anchor: any,
    weekly_anchor: any
}

export interface StripeSepaDebitPayments {
    creditor_id: string
}

export interface StripeTosAcceptance {
    date: Date,
    ip: any,
    service_agreement: any,
    user_agent: any
}