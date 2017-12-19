export const slugify = term => {
  return term.toLowerCase().replace(/\s+/, "-");
};

export const getPlayerById = (playerId, players) => {
  return players.filter(player => player.id == playerId)[0];
};

export const persistInLocalStorage = obj => {
  Object.keys(obj).forEach(objKey => {
    const objValue = obj[objKey];
    window.localStorage.setItem(objValue);
  }, this);
};

export const fetchFromLocalStorage = key => {
  return window.localStorage.getItem(key) || null;
};

export const getRequestUrl = (url, params) => {
  var query = Object.keys(params)
    .map(k => encodeURIComponent(k) + "=" + encodeURIComponent(params[k]))
    .join("&");

  return url + "?" + query;
};

export const prepareFormData = params => {
  var formData = new FormData();

  for (var k in params) {
    formData.append(k, params[k]);
  }
  return formData;
};
