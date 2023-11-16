import axios from "axios";

const axiosInstance = axios.create();

axiosInstance.interceptors.request.use((request) => {
  const accessToken = localStorage.getItem("AccessToken");

  if (accessToken) {
    request.headers["Authorization"] = `Bearer ${accessToken}`;
  }

  return request;
});

axiosInstance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const {
      config,
      response: { status },
    } = error;

    const originalRequest = config;

    if (status === 403) {
      const accessToken = localStorage.getItem("AccessToken");
      const refreshToken = localStorage.getItem("RefreshToken");

      try {
        const { data, headers } = await axios({
          method: "post",
          url: `${process.env.REACT_APP_API}/refresh`,
          headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + accessToken,
            "Authorization-refresh": "Bearer " + refreshToken,
          },
          data: { accessToken, refreshToken },
        });
        const newAccessToken = headers["authorization"];
        const newRefreshToken = headers["authorization-refresh"];
        localStorage.setItem("AccessToken", newAccessToken);
        localStorage.setItem("RefreshToken", newRefreshToken);
        originalRequest.headers["Authorization"] = "Bearer " + newAccessToken;
        return await axios(originalRequest);
      } catch (err) {
        console.log(err);
        // localStorage.clear();
      }
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
