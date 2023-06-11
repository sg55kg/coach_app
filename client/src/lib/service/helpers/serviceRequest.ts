import { redirect } from '@sveltejs/kit';
import { goto } from '$app/navigation';
import { authUser, userDB } from '../../stores/authStore';

export type ServiceResponse<T> = {
    data: T;
    code: number;
};

export const srGet = async <T>(
    url: string,
    expectString: boolean = false
): Promise<ServiceResponse<T>> => {
    const res = await fetch(url, {
        method: 'GET',
    });

    if (res.redirected) {
        if (res.status > 399) {
            userDB.set(null);
            authUser.set(undefined);
        }
        await goto(res.url);
    }

    if (res.status > 399) {
        throw new Error(res.statusText);
    }

    let data = await res.text();
    if (!expectString) {
        data = JSON.parse(<string>data);
    }
    return {
        data: <T>data,
        code: res.status,
    };
};

export const srPost = async <T>(
    url: string,
    body: any,
    contentType: string = 'application/json',
    parseJson = false
): Promise<ServiceResponse<T>> => {
    if (typeof body !== 'string') {
        body = JSON.stringify(body);
    }
    const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': contentType },
        body: body,
    });

    if (res.redirected) {
        if (res.status > 399) {
            userDB.set(null);
            authUser.set(undefined);
        }
        await goto(res.url);
    }

    if (res.status > 399) {
        throw new Error(res.statusText);
    }

    if (!parseJson) {
        return {
            data: (await res.text()) as T,
            code: res.status,
        };
    } else {
        return {
            data: (await res.json()) as T,
            code: res.status,
        };
    }
};

export const srPut = async <T>(
    url: string,
    body: any,
    contentType: string = 'application/json'
): Promise<ServiceResponse<T>> => {
    if (typeof body !== 'string') {
        body = JSON.stringify(body);
    }
    const res = await fetch(url, {
        method: 'PUT',
        headers: { 'Content-Type': contentType },
        body: body,
    });

    if (res.redirected) {
        if (res.status > 399) {
            userDB.set(null);
            authUser.set(undefined);
        }
        await goto(res.url);
    }

    if (res.status > 399) {
        throw new Error(res.statusText);
    }

    if (contentType.includes('text/html')) {
        return {
            data: (await res.text()) as T,
            code: res.status,
        };
    } else {
        return {
            data: await res.json(),
            code: res.status,
        };
    }
};

export const srDelete = async <T>(url: string): Promise<ServiceResponse<T>> => {
    const res = await fetch(url, {
        method: 'DELETE',
    });

    if (res.redirected) {
        if (res.status > 399) {
            userDB.set(null);
            authUser.set(undefined);
        }
        await goto(res.url);
    }

    if (res.status > 399) {
        throw new Error(res.statusText);
    }

    if (res.body) {
        return {
            data: await res.json(),
            code: res.status,
        };
    } else {
        return {
            data: null as T, // need to find a better way of typing responses in these cases
            code: res.status,
        };
    }
};
