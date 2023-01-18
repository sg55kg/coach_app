<script lang="ts">
	import {auth0Client, isAuthenticated} from '$lib/stores/authStore'
	import UserService from "../lib/service/userService";
	import {goto} from '$app/navigation'
	import type {PageServerData} from "./$types";
	import {page} from '$app/stores'

	export let data: PageServerData

	const { state } = data
	console.log(state)
	const loginUrl = "https://dev-iubbkos4gue16ad5.us.auth0.com/authorize?response_type=code&audience=http://localhost:8180&client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&redirect_uri=https://localhost:5173/home&scope=openid%20profile%20email"
	const logoutUrl = "https://dev-iubbkos4gue16ad5.us.auth0.com/v2/logout?client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&returnTo=https://localhost:5173"

	let popupOpen: boolean = false

	const logout = async () => {
		try {
			await fetch('/', { method: 'POST' })
			window.location.replace("https://dev-iubbkos4gue16ad5.us.auth0.com/v2/logout?client_id=iVM0PNhPw0acBMwrb5IUyedtMjg7ejlQ&returnTo=https://localhost:5173")

		} catch (e) {
			console.log(e)
		}
	}

	const login = async (e) => {
		e.preventDefault()
		if (!$auth0Client) return

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
			try {
				await UserService.loginWithRedirect($auth0Client)
			} catch (e) {
				console.log(e)
			}
		} else {
			popupOpen = true
			try {
				await UserService.loginWithPopUp($auth0Client)
			} catch (e) {
				console.log(e)
			}
			popupOpen = false
		}
	}

	// const logout = async () => {
	// 	if (!$auth0Client) return
	// 	await UserService.logout($auth0Client)
	// }

</script>

<svelte:head>
	<title>Coachable</title>
	<meta name="description" content="Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable." />
</svelte:head>

<section class="flex flex-col w-screen mt-0 md:mt-8">

		<div class="w-full md:w-9/12 px-16">
			<h1 class="text-5xl font-bold tracking-wider">Connect with your Team</h1>
			<div class="w-full md:w-6/12">
				<h1 class="text-xl text-wrap w-full md:w-2 my-4 tracking-wider">
					Track all of your athletes' progress and stay organized across multiple teams. Use athlete feedback and data from your programs to make better decisions. Write better programs with Coachable.
				</h1>
			</div>
			<div class="flex flex-col text-center justify-center items-center m-2 mt-8">
				{#if !$page.data.user}
					<a href={loginUrl} class="justify-center text-gray-200 bg-yellow rounded p-4 px-6 font-bold text-center hover:bg-yellow-shade mx-2 mt-5 mb-2">
						Get Started
					</a>
					<div class="flex flex-col items-start">
						<small>Already have an account?</small>
						<a href={loginUrl} class="tracking-wider font-semibold hover:text-yellow-lt self-center">
							Login
						</a>
					</div>
				{:else }
					<a href="/home" class="text-gray-200 bg-yellow rounded p-4 px-6 font-bold text-center hover:bg-yellow-shade mx-2 mt-5 mb-2">
						Go To Home
					</a>

				{/if}
				<button on:click={logout}>Logout</button>
			</div>
		</div>
</section>

<style>
	section {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		flex: 0.6;
	}

	h1 {
		width: 100%;
	}

	.welcome {
		display: block;
		position: relative;
		width: 100%;
		height: 0;
		padding: 0 0 calc(100% * 495 / 2048) 0;
	}

	.welcome img {
		position: absolute;
		width: 100%;
		height: 100%;
		top: 0;
		display: block;
	}
</style>
