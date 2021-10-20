package com.mzj.mohome.util;

public class AlipayConfig {
    // 商户appid
    public static String APPID = "2021002105674628";
    // 私钥 pkcs8格式的
    public static String RSA_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCkxUuX46vZbDUArTJSk7nRTDwmrmxLCKBxdMWP+UevIcSq/E2EsNmceRGTkzT74razcblr8VX1nz5sLiN3VXhnpLdJJJJhFfh2NNNudm+dalbEMZwV7L8Z0FUwqme48J1/QStd9wiG1VRUmGtK5Z0dcFxeK/DGNk5WTqhrDndsN5DzEW6hIbk/gPGTk56dww3QWItLWUyjwOyVpYXYyX6RjoPUWFT0HUu4p+ZfIKI9gy8ca8XexPXEBhRz4tSiIFsr/k068z8NpIQ472BdYgIdjJbh6yEKZRsCh7s4RZSWsUm/IvKktpol7qGXtj64sSSCrlhtx9+rsuAThSkk4hFBAgMBAAECggEAc3xNJWJPmo56/8Rk1vfvb0DNkBcEk/qx6ZN8vRJtKdSzMVyxQGVrwH4i0ZqFsl4Oy+rTmVKHeyZn7A7SkNt5DrumMz/M0PtDMCaKYovkYyKbOdvDmKJcts0MSJXHKRqMaRrWOH/inA59kuVZnh8dZfrQ4rhNg7+gXGG5LX/wOAoA/60w37HHtaplEBxEGwxm9N+gxN1D2M5YDLpj4RZL/hIYLJ4ZJ1DEOT2chzpz1hkHGmejONdDsSvSPHX26GpPb4WY4aY1n3fyILGZf6fk2D7oq7fQ3KafgcfsOSI4qSTwYF4DDsKK63Qh8XquIFywsj6J5lXoK07Abin1sf5iGQKBgQDhv4p33g0/jj+AGzUazCXX2rhZj0uzUPSy0zJgGuJWlKxeRrpXKm1lZLW1Fxkr+VdjgAk47dr4Bm80bmrLaC5qGHmlOdCOhkR9ZGhaz5aJReESGepvomxKV9UadDlyuEBKfTugLRnLfupMesN0wqYKhiuebR7f2EReJ+1z69wfGwKBgQC62eAcJ0KrYuJwLKTkauWeMZn3JW5m1GhIMC8VfkRbSvPVr2DPVTI8ymwf42TL1Zf8rjzHAuP4GRqvVQrIQ0pHKTnFtr/TJsKhVqFK8c3PXDmE4MomeXarJUoTEQiFogqH2YEF/5nmWYhA+UEJ/dSFz8uicf+D3BZaE+3qR64q0wKBgQDdSni/BeGwwo1G0+TbrvENxm7eBBTt41brkRIZmWD0BGtvQx/Y4FM6iF2C9fCL5bEfbVwc/TSf2xbZq79uQ2L0R6e/Koxhmga7cFjHZCSpzeFScCsaYXqL6cuRUgsahLkpC9gOLQfTLkcYz2KYK1K+kEVj2I3iWcqzDaq77qHprwKBgDavBPO4Wb6m1fZWWnuu/Txq79Bw1/qoN0zJLT5xEbhWGz5ycTa8jFNWOoaqmhk9jZmZtJfZtLYrQrAFh35MkzR4UDwY29MECPoGzHWOhtkk+IB2+TKBYcV8yE0EMyZI4iQX97a1s2jC0ymQyQQPkV8IDWShUFJa2v/JzVs/Vy2ZAoGAB0Rga3BnPD668L4IZvXJd7nvuInQzTOqQNP8peEi24+MosdQzecyo/7Ss7udiPiMc95hTLT/O301KqNIQ9Ye2kgds1K7FpplYeQ2W3koGIavFerhz66qYHmOYlomiPoJvbxpO2ssuZrW5ycUJZOUSx++d8Z5Ju3WuCyFVoyCmEo=";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://wx.mzjsh.com:8099";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    public static String return_url = "http://wx.mzjsh.com:8099";
    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";
    // 支付宝公钥
    public static String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjyNoQBhBI/IAtpr7HODvs81QdnwtouH7i8S8JcqLs2LXpFJ9TUimndvJPTdvSMA+ksW19H74rwluYzITP4xY91WVmfRmOZfb67dlIAiOdtVotXKuD28tykBn8Vxz3uh7cdbUQV11gX3Fs/ZRLNrNOY1layCCmDLl4RuCKqn5srEr4WWHm37lJfMSzkZf0KvduVQS0LLscDXPHMKrdmbGAgzoLGc5p0LkfdweYMHYpLvUyQaS+MBCfO1E2Q4QzpBEjD6UodkRIJC6MlydXDs1d0MKhEulLJdC8Hg4f2Cpo5nWfpskvqxVRCVflSf0wkWRBax8gf100OTmyxyvBN5eSwIDAQAB";
    // 日志记录目录
    public static String log_path = "/log";
    // RSA2
    public static String SIGNTYPE = "RSA2";
}
