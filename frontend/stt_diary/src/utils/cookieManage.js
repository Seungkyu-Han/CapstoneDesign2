export const setCookie = (cookieName, cookieValue, expirationDays) => {
    const date = new Date();
    date.setTime(date.getTime() + (expirationDays * 60 * 1000));
    const expires = "expires=" + date.toUTCString();
    const cookie = encodeURIComponent(cookieName) + "=" + encodeURIComponent(cookieValue) + ";" + expires + ";path=/";
    document.cookie = cookie;
}
  
export const getCookie = (name) => {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

export const deleteCookie = (name) => {
    const cookies = document.cookie.split(';');
    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            setCookie(name, cookie.substring(name.length + 1), 0);
        }
    }
}