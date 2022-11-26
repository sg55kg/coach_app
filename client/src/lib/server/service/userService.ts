import * as devalue from 'devalue'

export default class UserService {

    static loginUser = async (username: string, password: string) => {
        const res = await fetch(`http://localhost:8180/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                username,
                password
            })
        })

        const parsed = devalue.stringify(res)
        console.log(parsed)
        return parsed
    }
}