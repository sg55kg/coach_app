import { sveltekit } from '@sveltejs/kit/vite';
import type { UserConfig } from 'vite';
import mkcert from 'vite-plugin-mkcert';

const config: UserConfig = {
    server: { https: true, proxy: {} },
    define: {
        'process.env.NODE_ENV': '"production"',
    },
    plugins: [sveltekit(), mkcert()],
};

export default config;
