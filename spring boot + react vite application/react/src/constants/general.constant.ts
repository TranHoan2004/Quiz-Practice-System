export const APP_TITLE = 'Quezee';

export const PASSWORD_MIN_LENGTH = 8;

export const REGEX = {
    EMAIL_REGEX: /^[a-z0-9]+(?:[._][a-z0-9]+)*@[a-z0-9]+(?:[.-][a-z0-9]+)*\.[a-z]{2,}$/,
    PHONE_FAX_REGEX: /^[+0-9-]+$/,
    PASSWORD_REGEX: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{8,}$/,
}

export const SYSTEM_MESSAGE = {
    EMAIL_REGEX_MESSAGE: "Must follow the right format of email <name>@<domain>",
    PASSWORD_REGEX_MESSAGE: "Password must have the upper cases, lower cases, and special characters",
    PASSWORD_MIN_LENGTH_MESSAGE: "Password must have at least 8 characters",
}
