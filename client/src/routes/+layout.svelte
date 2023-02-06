<script lang="ts">
	import '../app.css';
	import {authUser, isMobile, loadingAuth, userDB} from "$lib/stores/authStore";
	import type {LayoutServerData} from "../../.svelte-kit/types/src/routes/$types";
	import {User} from "$lib/classes/user";
	import {onMount} from "svelte";
	import {goto} from "$app/navigation";

	export let data: LayoutServerData

	if (data.user && data.userData) {
		$userDB = User.build(data.userData)
		$authUser = data.user
	} else {
		$userDB = null
		$authUser = undefined
	}

	onMount(async () => {
		const mobileDevices  = [
			/Android/i,
			/webOS/i,
			/iPhone/i,
			/iPad/i,
			/iPod/i,
			/BlackBerry/i,
			/Windows Phone/i
		]

		if (mobileDevices.some(d => navigator.userAgent.match(d))) {
			$isMobile = true
			console.log($isMobile)
		}
		if ($userDB && $authUser && window.location.pathname === '/') {
			await goto('/home')
		} else if (!$userDB && window.location.pathname !== '/') {
			await goto('/')
		}

	})
</script>
<div class="app bg-gray-100 text-textgray w-screen">
{#if !$loadingAuth}

	{#if !$authUser}
		<header class="mb-4 p-2 bg-gray-200 text-textgray flex justify-between align-middle py-4">
			<div class="flex items-center align-baseline">
				<a href="/" class="font-semibold tracking-widest uppercase text-2xl ml-3 text-yellow-lt">
					Coachable
				</a>
			</div>
		</header>
	{/if}

	<main class="w-screen m-0">
		<slot />
	</main>

	{:else}
	<h1>Loading</h1>
{/if}

</div>

<style>
	.app {
		display: flex;
		flex-direction: column;
		min-height: 100vh;
	}

	main {
		flex: 1;
		display: flex;
		flex-direction: column;
		width: 100%;
		/*max-width: 64rem;*/
		margin: 0 auto;
		box-sizing: border-box;
	}

	footer {
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 12px;
	}

	footer a {
		font-weight: bold;
	}

	@media (min-width: 480px) {
		footer {
			padding: 12px 0;
		}
	}
</style>
