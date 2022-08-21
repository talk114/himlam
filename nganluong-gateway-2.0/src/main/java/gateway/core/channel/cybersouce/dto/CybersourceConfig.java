package gateway.core.channel.cybersouce.dto;

import gateway.core.channel.cybersouce.request.ApiKey3ds2;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CybersourceConfig {
    public static final String MERCHANT_ID = "nganluong_vtb";
    public static final String TRANSACTION_KEY = "fMFKDDWtNHQPr+6nT2YrkBVDqNFZxyRUmF0bM/ObdBPM9bpMJzYsSHx8Wghl+D2qzwfyVuLXS9IkTnn4VZFXvs6c0ltlCCuyCRY+M7vsekLGYTZPQB4SvZWbDq1HC4/emBo2mYGrozLREPyYAFBFy16L4OdK7XzbCYucgEStZyCwrlNQgdZGH3rJ0TCRbEiZR3w7Z1Z7oPvih8ZTZ4WeG3ZPM1oB89nlXBAcHiBT57a6cArudrvQ6BtqCTFuKVsZN4DtDZk73RgGTg4Vj1rNAm5RCL0MsSK+WUMmW0v85EYbpPitNZMV19PUDA8SkLu3RUVUvwBvm446PmiqIhKEwg==";

    private CybersourceConfig(){}
    /// Key payment cybersource TEST
//    public static final String ENVIROMENT_KEY = "KEY_TEST";
//    public static final String WSDL_URL = "https://ics2wstesta.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.181.wsdl";
    //    //
//    //vtb
//    public static final String MERCHANT_ID_VTB = "nganluong_vtb";
//    public static final String TRANSACTION_KEY_VTB = "fMFKDDWtNHQPr+6nT2YrkBVDqNFZxyRUmF0bM/ObdBPM9bpMJzYsSHx8Wghl+D2qzwfyVuLXS9IkTnn4VZFXvs6c0ltlCCuyCRY+M7vsekLGYTZPQB4SvZWbDq1HC4/emBo2mYGrozLREPyYAFBFy16L4OdK7XzbCYucgEStZyCwrlNQgdZGH3rJ0TCRbEiZR3w7Z1Z7oPvih8ZTZ4WeG3ZPM1oB89nlXBAcHiBT57a6cArudrvQ6BtqCTFuKVsZN4DtDZk73RgGTg4Vj1rNAm5RCL0MsSK+WUMmW0v85EYbpPitNZMV19PUDA8SkLu3RUVUvwBvm446PmiqIhKEwg==";
//    //stb
//    public static final String MERCHANT_ID_STB = "nganluong";
//    public static final String TRANSACTION_KEY_STB = "3J0H4qubeUmfnVM4don3r/I2qCX8EHn4KYdG8apehdzO+YofoLYisr62OZlsx/N0kqQWk5Av9EI4s5vzgV42wzweihWdAqCb54u/T3QLZOfIdnUvTDVGCh88T1ZiXfuHdhNOd6IoF2TScw7oShZHv5yyCZ56GchWNvi4YcQ7syUqUPM3LJ8NRRzCeu4T5tQvwNZdODuD5XoOfb98UQ2AQZlqZqmd3zbkyiZ6QbZYFNopYJdYdM1d9ODlOX16v5mu1qj54XTNCHtwMWF25x+uc3J/493P4S6GOvDEUBBbvQsd1Wa/Bdhr7j/5WtUCaHtOoiv3frOmA6NIBZJAAplNKA==";
////    public static final String TRANSACTION_KEY = "QYyE3Jrk10P4UkRaLunDaYj1+RMHr0UE4gzHM8o4KNWIzhPS98D9MyGevNMaAdQ2ewqYEdJ17OujrUF0tPpH2bv1PvxZgwFY+eu/C9phXD2HmDJFI1Z0Xr7UeuYfegm07gn2f1W+BgSrgi24OMGhaxnYrffFcd3Jp43ZZHTv6e0nFhcMh1U3rIwYkYiGnlmHqQtbluCLhjNzpxNgyJXL4qoGmMntqq4zkmCOUOcOAln2Psi5tLS1aDNAb1Wi1R1sUUpOglEY87gDpmY1tBux+/B1yWIUC/x0LUZMG4eXAIvCABxHi9zUM4YdPo9/cL+vm5Iea+FJZrSTNcY5/J+yMA==";
//
//    //vcb
//    public static final String MERCHANT_ID_VCB = "nganluongjsc";
//    public static final String TRANSACTION_KEY_VCB = "H4rlJTHeZwF+ElWEDKr4DT8OUMGDtAwgySmbj8BJdkp56smNlgPCZMzTwIsVob4BBCRnfGqnb1aUI6NnCHdDkMfqsVxCVBcZCS/ArxPaB53APq+qgBXP9l4wLVJIoRycqNU8Ch6cXvST4QDuYAMk/tSIXboH4kzXYOTqPMsAzxaSkXTne7m8MrueHFUJS7UXvNNT7UHLMqpMFrrIwD2DDk5toFs23WE1jm6ccC2lj/NZkdu/rz7mAauTz7QDkOZ4toG/kJJ7OCLYt1XcD5JBm5vxjESl2s01WUCxPedRRzvZhZrcjJgiUY1Cv5ApYa68NHNpJFVuyPSCtmjMQGJ/qQ==";
//
//    //STB Tiki
//    public static final String MERCHANT_ID_STB_TIKI = "nganluongtiki";
//    public static final String TRANSACTION_KEY_STB_TIKI = "ENyKEjpTcgsbBg25fGL6McdHOsjVPxkbguQGyjw1sZZd0nkRtw/rdD94LZDfp7R8Wf4jtrV3LL3oi8WhKXzZ7EMAV4NAWO5w1aC4+zBQkHB4L+y5ST12ITzy5k0i8tobEHltjqxE0zyD5ZiZwZ9agjIjVAeG7nYSpPYtD3pFvkg5ISnt2BTA2KDMXFDy3NJ5m9O1UNHEpppFPeYHuHEnvoJzniRebGN6zrJKV+ytEumSf9LykU69yg1ekI0++B1fRx1jLGG04sNsXCMnMeCbrwCnadq2/mE2VJkV8EbZzux2IjFTHVewN99NXF2sXCWh4G3n4lek7C1zxO7S4RqpUg==";
////

    //// Key payment cybersource LIVE

    public static final String ENVIROMENT_KEY = "KEY_LIVE";
    public static final String WSDL_URL = "https://ics2ws.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.181.wsdl";

    //vtb
    public static final String MERCHANT_ID_VTB = "nganluong_vtb";
    public static final String TRANSACTION_KEY_VTB = "Yh3ANNHnZI7duC150ekEm8aAI3zD2xsBG+tmuZE6QG+ZTjhXNGFaP0ALzPDdhTKpj/yeyf3nJaS05b7YQBwqdtVxGH91Au7x580VZPisE/tMy0CsNZuYgiDa8HlQ0fqQs0LMLJi+tlNl5dHGCF36lRhlh8MRG470ggm6gbKIRCmITB6VJihqzqvHiQVZ993YquC53nBofil2LfuZX85oNcPMJpBFICAx/rgzvuMRhbQOeKWbnMxauXe6HcoL848J6/VET5QVr27NvLAH80UtYpOowOIT1WrwpN2VrJrEGoR4alMJjuuBShz0i/NcVd7BCo7sjYaw6caHcBT4CH+IAA==";

    //stb
//    public static final String MERCHANT_ID_STB = "nganluong";
//    public static final String TRANSACTION_KEY_STB = "dZta3imOc4zH3BZMEchgkhyyPur0x2ICbCX+PfW7wR90LiHq440+JgteyXPQs2+5QNo1bcCfFxMi/ZrKRCYPCYFqohoLSwn9827zRB2KwJNfX/qZP7Ee5pb3f1FlRwWH1pbUReAb0SJfW0zLBrCMENiXemXuvVZk7W2ccREZxAPdqfQJttcm1PRX/oQveFjjwfOepoBTUhdwB2RM8IVuXGPPbTxi1NwOHtcKbpAlK5sEMCpv3HSjLegGBm+sZ/85iOGAtoPXIPCyMwrY23zozt6W95dPeyyFbWla4ceSD6hlZFxwL64rv4+9KM7RBvrpU6PCNOuvbdqa99a6kxPYTw==";
    public static final String MERCHANT_ID_STB = "nganluong";
    public static final String TRANSACTION_KEY_STB = "0OvBoMO5wcGoRGk8RsG94ACnnv6C6yQi2besbi1nIqKXp7iTVIBDkChSu8F4bZrQ/3cGHNrW7FSUJah0XK413Dj8tVJeNRk31eNfNm8XFPgeQRxHJuPYd5LEtyhUR4xCMlspFx8+BfIARXt4MQaY2vrQ1vgn1TxduzB+ghTB+xNATK71dSni58BnAHwLgpLCQAjwXT+I4vTVfApFCyf1x2/Gi7QVmZbmUrfVl7fSWctrLNlzwufJN5HYnqvzgd8dPwrHu2HmaCspOq+SgG7y7h1mIjkppLRsC5R4SlYtWyLed0/J987iuUA5af29tFmkHkhcWAUyeGxhA7yBXx/cbw==";

    //vcb
    public static final String MERCHANT_ID_VCB = "nganluongjsc";
    public static final String TRANSACTION_KEY_VCB = "G+Lx0JfzaSIWrCXfYD5yzivq7h2JBaTjAQX4HoWHu7umSWubtWImVhrhNYi8TSNOVLYZM1bEORv0QT64bzHNlrjEWy7rU/u4abBhdgJcRDT1WsSTyC8p7vfiXhrd2d6OOimk3eWE4XAdBFH/rFE8n7jmhEI67KPeINWqvLVacSZM/1hZDEFUWFW6MfdIlp3CJ6oiESMI+jJ/oz/t8GUx6OEXBsPR+NWHkx/SU0ACt1FbvLQXv1P1kPFdW1Uv6VPBra6nxZ2fk0Wydq23hB68ISaFUbtmUJZoY1Nf1ZYJXNYOIyUJvPdeAS+pJiuGoM0+bAGNTpuUHY9enRhCoE0CoA==";

    //vcb_sgd
    public static final String MERCHANT_ID_VCB_SGD = "nganluongsgd";
    public static final String TRANSACTION_KEY_VCB_SGD = "s0iwsrJ9qWN0gA/fkj2HFcJ9HjHGO1VWXWJk/VH8C+ddwvSQLxmtRTx0MFxAAzi4bwlDXSfZdflWIow+CDT45dZGy4lceWFhzhDoi9xLHU8JAfcoVvXo74LWU8FOXPB8iC4wyDrEHWzWLjFvrt3VIMOYoqS6Z/KHmcDrVKKHhzGmS8C8WONpQ0ZyG8nI0L9qpXclbQIeqMW69AZerLmlXH2E+fRoqyx298vXbMzX3A6FsftAylCUL/xUxAwPy7TeaF40UPx/wO+WP8jNggeoqJELiJXAp9PtEW+dBq/ZqKP3hy8d2FnGrEHZMH0CbxpejgthOP5Gnwn1KtfLM8r0ZQ==";

    //vcb_nganluongst
    public static final String MERCHANT_ID_VCB_ST = "nganluongst";
    public static final String TRANSACTION_KEY_VCB_ST = "7f3yYP/V5ff9Ci7s00la8FOQiLzMZjYpJMcKevC6B+O7H3G3XcaVCyCL4ZAlwFgDuTFgbSz4EVZgNl9XdNbpgMBthkR5hs0KeIxoOshXxvVzeG1CzfND9m7dp8KFy96oe11SlYG/4/A83E3tSyNe1bqc1lZwSawP9nNTTnDUJr+tmZzP4ZB7ueK2dLFFjbxrwb5kZeMgYJSYxTBj3XvH9Td6OSPUTcHmEnEt+h3Syi2lV/sT0n3f3jPqTLhZxlu1I9LHuMnKOazx+Y+7eZR8ppcvvVJN8aG5LsDzYqwA/okAQvpHC/MLTAzrOmc5SSK8H9qd5Uh+UZcpSEokyGVo/g==";

    //vcb_other
    public static final String MERCHANT_ID_VCB_OTHERS = "nganluongothers";
    public static final String TRANSACTION_KEY_VCB_OTHERS= "dc2/ydIBiuKgns799NWLUU1/I+1YnhZgcvQi8C5SnkxkHpe2w865l0tJGJktGhSU0QPk5vKFzETkVEDxTPfulOUzM82QZTYYz/R4uSU6yTs3EVIS1+NaN6rBzmumrDRjr/h9j1wKOQK0y4zBaop1xHrYlMv8kdQffVG0LjHVaIF71UXjTZ6gMfVRq00AUh3++FfhCLcIN8c+KHn6DHex6QlWYR7Bd7cm9b0P090tMDYjUrvuEOEMimSYhQT0+NZwlNsDH9f/xYO6Z3y8rENmkEuHCmfDDPLQJbKgtX/gwCT/M+xJoY5HCDb7VyPsB0QgnyETdnm9tt3CUhwDqXj/KA==";
    //vcb_th
    public static final String MERCHANT_ID_VCB_TH = "nganluongth";
    public static final String TRANSACTION_KEY_VCB_TH= "AD1HLUX1AqHwz6z5rP7NtDIFdQhBOVBESEeJvZ5qTZWcpWejmOO5Sv5eWhjUyCSGOiVlmnkMqkevD6K5j284qPsDh07c4F6tmCBgPv2CHo3rHssrVt7YtHkLhEZrvPwFIMdI2lrbOyP0+uQzLaH7PaCLpNjnvv6RKMZ2Y7SZbGEJkFB5DUiqKbm/4QU5nn9qribM7VymjFzT7dHPZiTAPy7RUhPcvmP1XPmCataoPHAIVYLkMH/bMuhl/IKEoQuA7G4JHxfCErceyyGBbls5QraKYRNqC6CA8JzuDJq28FOqIEIb5X/6Y7Q8FOQLI2LiLUYFSHDxdedX2ui4HuhDKA==";

    ////stb travel
    public static final String MERCHANT_ID_STB_TRAVEL = "nganluongtravel";
    public static final String TRANSACTION_KEY_STB_TRAVEL = "2DD33QTvkgSN7PnGAaiI4nNW5zIMYXcCACyp9vj6zHpnGoPCZWPgef5OnJjZ4ne43Y/TIXmszVNWt6VpduqvmvBiBMJHQ+lTpTSbXpFMC8LL62BE+CLZThLyOHy/hiQ7PfE5FW1pKTTwPJ5mlfnLEPORs8RObVn/7RGJEPzPNwxIAKGD4cskPxME2Cj3JHJ9dALFhnSAYt2Z/tyVQ0MXiaWgSw6D1WL4r6CryEe9D1YcWELGaJEfyubKc+jzvScp+vvyrAwo52q4/rQc9/KeVsn0mwYspuVusW+vd/fZYYWBFj2NvRWORg37Clcj0xHahiju83dQI2K2kyq+ikF5Nw==";

    //stb cbs
    public static final String MERCHANT_ID_STB_CBS = "nganluongcbs";
    public static final String TRANSACTION_KEY_STB_CBS = "PkD0dXM/3tjQBcGvDb9DcvDKWhDlsJSZW6W8O7aStdf08yFA1zoeYAY+d1Vf8vBeJEv5Zf+LUE99B8jU/o6q1f3nLIN3S9qFpN8FufO9QvVkKg9DiciuHqpzlQI0iM0PgeUIEcufzrrn+Z5IrczlycMzRNT++TnDIeM8qyK5iKsIwXL/VVOHjExoVEmP+kN1H2d95Yt4fgjDoscVgBGUNGuJiCzSGi7REbhUK3v4upWyy40D5WXx6Rnhqxb9OgSaUzd41OHGebqVUkW+GkoseM+yhwKptyALQnEE75PnMm/O8W3qohRlRn2k84OmU/4Gr9M2vsdGUTzjhdvFnAyUGw==";

    //stb tiki
    public static final String MERCHANT_ID_STB_TIKI = "nganluongtiki";
    public static final String TRANSACTION_KEY_STB_TIKI = "MXhkHU0PzdrnmGFF4++WwpRqaRUiswVHPqg6L8sthR8Zufs0BqPRbnK3mR2t/kBkcEUDovz8PSVY+s6Etp4XMM89ddZvOP5UubYRDSFVTYPSILj3ptXoYmwOXcD5O6CsHaY7QGkcj0wasqhvlrb7uZurNwbsNhIB2KGGcWjt8E5YvkEHDkA5y3oJtlWCKNxv6En/7YC9bahVbfkdw67ow1RdaGmtE4vJLXeRcwidZbhTHaGnIbTJdGtSd5SsWt3t6L0HmPM71GzPCM9q8nfjh6jK8YR5cbJE5psyzvJxELFWIM3n88JHpVhD5GQ9iqIqM8lrQT50sPWfmS3sp3dEnw==";

    //stb bh
    public static final String MERCHANT_ID_STB_BH = "nganluongbh";
    public static final String TRANSACTION_KEY_STB_BH = "t5eDbR3/ASt+r9cpmXBK7CS3nl40qhiFHIgFNJhfywXAa3/8gIvXlKqYrzMQPCLDWAQkYTu8uNDidIQiK7CTs/3WH1nubCokC/EOSF2e4uZ11WmriCbE0uoPJK5sTr1HYjHiWI5QHPXbTCFtLSp0sNi9RITeSk6gi/OdKPBN/hdYEKqm/bMERLkDf/q6u8uEacUR2enfNhoDTTt2qz1gypX1eeXyKa2Grov/yDuITJuJaHDwNhR5m/0aiQRq3K9qyMQvfSfP6DzlfQW/a2XvdtgzsQt4/VVKI3WOkc1MkruPjXo4+Vt2tUpbJf2hB5x6VQVDZnbnT0yRxk5Q2P9zfg==";

    //stb dvc2
    public static final String MERCHANT_ID_STB_DVC2 = "nganluongdvc2";
    public static final String TRANSACTION_KEY_STB_DVC2 = "0MiWoTvFgZlQmPKx3jQY7BL0dOxpvjJp22S15XcwMlJEtZayuoiWGKjGdyvGCNgJIuzkt+UsmmJuSFoOtBY+cxbsN0laMR7cTJ0fdNYPIgkeN10HHDB4IiaLfnSXQ1etybrxxOA0aUFoqQHDqEYmNWdMu41mz+st+6ohmHMVrqCnzxdA2c09rhw6LDWkwTkCSfFz2cQ4gpGlPGJl5VunR9TXmccw9dv9V7NLvcGPTyDHVoT9kGWuDyucDiVlbDr5hsL1dQL8NCdMS8aj9dMCdW761vVunVxOWY83pbGc+tLDcuO/XKq0Wgw8r1AkqQQhwhJkERCTaKg61YOafUPHtA==";

    //stb dvc1
    public static final String MERCHANT_ID_STB_DVC1 = "nganluongdvc1";
    public static final String TRANSACTION_KEY_STB_DVC1 = "r++LrDKKnS+//0c6awrvHlvnXXTzcFL2RfQRti2uw36Rw0wdpEdxoSebYRB1MvZHR6OKZ+SrFg36IGNwKRMo6o3tBFIkreh1xwYFEuh89gNXZq/28dzpwjdhrL9oGzHwODSrYiShgJEmZM9WFoqyq5B7Xh9nwgTy2TEfeQv8OsBieqeUXAbXttq6zNkEo7tAk5E/DTCGm0LoXEGQKSYjJO/ALfFDMsPsVE6caN8vSXwCmrkrczctmnZeNFlgrwSzMAKi/X34fDLw4NqOHRU7XHTE5vmu8qpKfsuGGwKOMZIQFsjYTMv/D6JeIa+OOKQJ1lpxbCSW3N9jIrSL3jUUBg==";

    //stb dvc1
    public static final String MERCHANT_ID_STB_VISA = "nganluongvisa";
    public static final String TRANSACTION_KEY_STB_VISA = "VKU/1SXaATu8QhhlDxumQrBEvg6iUpVRBPFfc0kRvMrC68tWA2fgqXuGYqzvoQO9GcQkPCoHcTAdo7L0RiFmBNT2niXFfPDskYys7H1bcjSri3q08xNbSvtra7Qg03Ud5f35ynTmVUDKMc0cKKWlFXAfY+QLL7ieqzuDsV8+UOkJci8X+GiGGEJZk3ovzdebiDP8ZfwSdaBK3C9RgqHCwsfjLbCAZREtsqZmkw1Aw7eGzO6YyrvXvzBh2mzoKlcWhLtsCAAt5iCj5hr27vjfuW0L02xsDhVHIBCK4JmPaFZJMP0m6K1F+PTs45Gb0UyX2Yrjk8A+l5h6Vhzkcf62Mw==";
    //Channel
    public static final String CHANNEL_CODE = "CBS";
    public static final String SERVICE_NAME = "VISA";

    public static final String FUNCTION_AUTHOIZATION_CARD = "authorizeCard";
    public static final String FUNCTION_AUTHOIZATION_CARD_3D = "authorizeCard3D";
    public static final String FUNCTION_PAYMENT = "authorize";
    public static final String FUNCTION_PAYMENT_3D = "authorize3D";
    public static final String FUNCTION_VALIDATE_3DS = "validate3Ds2";
    public static final String FUNTION_CREATE_REQUEST_JWT = "createRequestJWT";
    public static final String FUNTION_DECODE_JWT_RESPONE = "decodeJWT";
    public static final String FUNCTION_CANCEL_AUTHOIZATION = "cancelAuthorizeCard";
    public static final String FUNCTION_CHECK_ENROLLMENT = "checkEnrollment";
    public static final String FUNCTION_CYBS = "CYBS";
    public static final String FUNCTION_CHECK_ENROLLMENT_3DS2 = "checkEnrollment3ds";
    public static final String FUNCTION_SETUP_3DS2 = "payerAuthenticationSetup3ds";
    public static final String FUNCTION_SSP_CREATE_TRANSACTION = "SSP_CREATE_TRANSACTION";
    public static final String FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL = "SSP_RETRIEVE_PAYMENT_CREDENTIAL";
    public static final String FUNCTION_SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE = "SSP_RETRIEVE_PAYMENT_CREDENTIAL_DECODE";
    public static final String FUNCTION_SSP_NOTIFY_PAYMENT_RESULT = "SSP_NOTIFY_PAYMENT_RESULT";
    public static final String FUNCTION_SSP_HEALTH_CHECK = "SSP_HEALTH_CHECK";
    public static final String SERVICE_ID_NL_DECODE = "934b8de4541c46a78ea19f";
    public static final String SERVICE_ID_CBS_DECODE = "08326c4b85ad4a65aa8987";
    public static final String URL_SSP_HEALTH_CHECK = "https://api-ops.stg.mpay.samsung.com/ops/v1/internal/health";
    public static final String AUTHORIZE_SUBSCRIPTION_3D = "authorizeSubscription3D";
    public static final String DELETE_TOKEN = "deleteToken";
    public static final String FUNCTION_CONVERT_TRANSACTION_TO_PROFILE = "convertTransaction2Profile";
    //API KEY_TEST 3DS V2.0
    //stb
//    public static final String API_KEY_STB = "741385e9-3e64-4580-8386-cfc1b0f4fa24";
//    public static final String ORG_UNIT_STB = "55ef3f0af723aa431c99b32f";
//    public static final String API_IDENTIFIER_STB = "608b6dd10d61937ce238813b";
//
//    //stb_tiki
//    public static final String API_KEY_STB_TIKI = "78bb5e73-3921-47f1-8834-71cf79743308";
//    public static final String ORG_UNIT_STB_TIKI = "603dc6aea19e431ec25104e5";
//    public static final String API_IDENTIFIER_STB_TIKI = "6092cd96d3978c192e31ef11";
//    //vtb
//    public static final String API_KEY_VTB = "f4fd8e83-30c8-43f9-9937-3814d8972e31";
//    public static final String ORG_UNIT_VTB = "5ed9fbceb0268436cad02493";
//    public static final String API_IDENTIFIER_VTB = "60be11fd8d8e5e1faf97719c";

    //API KEY_LIVE 3DS V2.0
    //vtb
    public static final String API_KEY_VTB = "ed75b7c2-31e5-43f9-a396-bcde963c3c2d";
    public static final String ORG_UNIT_VTB = "5ed9fb816aff5422b1405dbb";
    public static final String API_IDENTIFIER_VTB = "60be134729bef65fde441d14";

    //stb
    public static final String API_KEY_STB = "18f5f519-49cd-44f7-9278-8f37f3cba5f3";
    public static final String ORG_UNIT_STB = "56099d00f723aa3e24d81a99";
    public static final String API_IDENTIFIER_STB = "5f6c3fd58a97e078de0f75f3";
    //stb_tiki
    public static final String API_KEY_STB_TIKI = "328272a1-62a1-4b30-86a7-900a8c83b534";
    public static final String ORG_UNIT_STB_TIKI = "603dc651287c36651cb3d554";
    public static final String API_IDENTIFIER_STB_TIKI = "60daff0a4ef5f235e4ab7778";
    //STB Travel
    public static final String API_KEY_STB_TRAVEL = "01ca1312-d514-4c9d-a636-c8a2349a49c7";
    public static final String ORG_UNIT_STB_TRAVEL = "56099d86f723aa3e24d81fc5";
    public static final String API_IDENTIFIER_STB_TRAVEL = "5f6c1145daafa47007df23b3";
    //STB DVC1
    public static final String API_KEY_STB_DVC1 = "a8d5e21e-db68-42c7-9dc8-166a357356f6";
    public static final String ORG_UNIT_STB_DVC1 = "6087e0a127c05946b42af655";
    public static final String API_IDENTIFIER_STB_DVC1 = "60daff0b55faeb2f17f6d595";
    //STB DVC2
    public static final String API_KEY_STB_DVC2 = "30debe0f-78c7-41b2-981f-48c2273c3956";
    public static final String ORG_UNIT_STB_DVC2 = "6087e0a127c05946b42af654";
    public static final String API_IDENTIFIER_STB_DVC2 = "60daff0a55faeb2f17f6d594";
    //stb nganluongbh
    public static final String API_KEY_STB_BH = "065401a6-d63a-4dca-a5c9-798994b908fe";
    public static final String ORG_UNIT_STB_BH = "60a784a044ef065b6c90ed24";
    public static final String API_IDENTIFIER_STB_BH = "60daff0955faeb2f17f6d593";
    //stb nganluongcbs
    public static final String API_KEY_STB_CBS = "09b5c519-7e7c-4fb7-8476-460a551381e9";
    public static final String ORG_UNIT_STB_CBS = "5ef30801ba637650d8a4fd31";
    public static final String API_IDENTIFIER_STB_CBS = "60daff0937f7700d4ca68591";
    //stb nganluongvisa
    public static final String API_KEY_STB_VISA = "03457f8c-62a2-4391-94eb-d62ae644da4e";
    public static final String ORG_UNIT_STB_VISA = "6087e0c602bb3860a31b0523";
    public static final String API_IDENTIFIER_STB_VISA = "60daff094ef5f235e4ab7776";

    //VCB
    public static final String API_KEY_VCB = "5fcf0d52-b56f-4628-9bd2-64b99f7bef79";
    public static final String ORG_UNIT_VCB = "5be97928356dce15343dcb02";
    public static final String API_IDENTIFIER_VCB = "5ed760725ee7156a36d60049";
    //VCB_SGD
    public static final String API_KEY_VCB_SGD = "b25af53e-94f1-402c-8d68-1869d4ad8b7f";
    public static final String ORG_UNIT_VCB_SGD = "5e6781416656c209186bacb3";
    public static final String API_IDENTIFIER_VCB_SGD = "60b67ab5e8aea069482f270d";
    //vcb_st
    public static final String API_KEY_VCB_ST = "893013fd-e612-4a4d-b599-f0cd27bd14a8";
    public static final String ORG_UNIT_VCB_ST = "60e429a1387f127f1711e76e";
    public static final String API_IDENTIFIER_VCB_ST = "60f1cc503b3e4818816e07e6";
    //vcb_other
    public static final String API_KEY_VCB_OTHERS = "0d02f313-64f3-4aa5-8349-f985dd57a92d";
    public static final String ORG_UNIT_VCB_OTHERS = "60e429a1f968a12ee64752cf";
    public static final String API_IDENTIFIER_VCB_OTHERS = "60f1c5b4bda3cd6179db07ee";
    //vcb_th
    public static final String API_KEY_VCB_TH = "8b2d3bfb-124a-4523-8f74-c086fb8ecf19";
    public static final String ORG_UNIT_VCB_TH = "60e429a1387f127f1711e76f";
    public static final String API_IDENTIFIER_VCB_TH = "60f1c94cbda3cd6179db07ef";

    //VCB_BAO_HIEM
    public static final String API_KEY_VCB_FWDVN = "c49b5d59-23a9-447e-83ed-db74cbbef279";
    public static final String ORG_UNIT_VCB_FWDVN = "5e71f121b5c0170e5a47d7eb";
    public static final String API_IDENTIFIER_VCB_FWDVN = "5ed76169dbce5b0383023cfd";
    //VCB
//    public static final String API_KEY_VCB_fwdvn = "5fcf0d52-b56f-4628-9bd2-64b99f7bef79";
//    public static final String ORG_UNIT_VCB = "5be97928356dce15343dcb02";
//    public static final String API_IDENTIFIER_VCB = "5ed760725ee7156a36d60049";

    //nlalothers
    public static final String MERCHANT_ID_SCB_ALOTHER = "nlalothers";
    public static final String TRANSACTION_KEY_SCB_ALOTHER = "yXLTsvHstj2LbR0P1FgBwO+9vmnRJa8ZdwsDH73OLAzRcAeElCqHwTfqfUTbY8svz5/28Qriy3ErLxC0wsP6nreZdhhZyEQT8CjxA3j5/8eP540CWGYzf7np/VgpqyFhhwN//Vivu2Urlsjb7l9tQkNallKnZXDHaKiOUOqyIeBEOh3vJega4pGJNHp3zi3obdS+ldRFhitM1QHLeZnpiM8Mexmse/zyJkp/jqGA/GfE0T8Fxx5ZzEFu0IvGI2fTdjCPtAYVkNjdfYQRj8LF7dt9KbvMA7FrgjoJJhGESGNdv7xrUou9vkbVY9p0rW94ICUd8s2O2z6vmPIJdYElOQ==";
    public static final String API_KEY_SCB_ALOTHERS = "19f53d9e-facc-4f17-98aa-387f8c7b17e2";
    public static final String ORG_UNIT_SCB_ALOTHERS = "6107a601a9f6804db5cc14f1";
    public static final String API_IDENTIFIER_SCB_ALOTHERS = "6127d18fb9e77f12438890a8";

    //nlvantaicong
    public static final String MERCHANT_ID_SCB_VANTAICONG = "nlvantaicong";
    public static final String TRANSACTION_KEY_SCB_VANTAICONG = "lIoIfqLttXLhbVUorES4Tqimcw3QRv512qsXWIyfwSmzPcS6LJlmlFDk9BDqxGtbph0W/bDdMTvz3fdS4EiKdJ/+N4F5JwchZ3eORrIK7znk9MAguIa4jnz+SwcRErYwJ/2Bp0idnUWASwCvU5wsjCA7gYDytej8vFsquSNkwWYYNyO6lfrTygsK3bmoNiBJqu9EETepIMbvJ2ZUcsi4FIcaKhUejOokR9/itMwu/3iknB62hfvwHONfaetw85r69ewADgw22i69teEIwQjkiVzUpTuRU8gZ5AWBT++q3aWOXblxdtVWi1gHUtA4xS1SPEFgLWrzS4o79CqVdkn18A==";
    public static final String API_KEY_SCB_VANTAICONG = "46d0ea26-77fd-40a5-a82b-61a630cd0788";
    public static final String ORG_UNIT_SCB_VANTAICONG = "6107a6016e7bae69631a0c27";
    public static final String API_IDENTIFIER_SCB_VANTAICONG = "6127d1acb9e77f12438890a9";


    //nlbaohiem-stb
    public static final String MERCHANT_ID_STB_BAOHIEM = "nlbaohiem";
    public static final String TRANSACTION_KEY_STB_BAOHIEM = "ug+hJbXjXicY++jdV/mNDfFXdawsgJ9SdNaMmxxMpw60ybntXIosVsWdt0LmK/mwtkssWG69jj/DZDkMywMB0tLl7CbaRLBd87QBvXF1ae18wVRx2oaXEP6DtW8HxX3jyO60MAjkp5ukPV5Mux2/97I3foA2JjWSaeeI+IH0LEnxE3gPgC3E0/kkdVoJ84DUjEml9Qh7i49sNAh/fIgNs+iwtfwudSLUvzMednitT1e4l+B9unjHs9Wo3AZ32khIK1yd1eHt9kkU5+ATGXOGeDj4irEV0x/59WDA10TKUqsVPXs3T4WZlZywqqON4ZDbQrlUEg3yZeloTeyW66Aa6w==";
    public static final String API_KEY_STB_BAOHIEM = "97419793-8224-4f8d-9b76-d26a61e962f1";
    public static final String ORG_UNIT_STB_BAOHIEM = "628204837d792c1d2b395d82";
    public static final String API_IDENTIFIER_STB_BAOHIEM = "6296ba53f623c7500e5ed140";

    //nlbaohiem-vcb
    public static final String MERCHANT_ID_VCB_BAOHIEM = "nganluongbaohiem";
    public static final String TRANSACTION_KEY_VCB_BAOHIEM = "1dC5bXaOvMRhsEChAgQZv7SZ6gx0eqg8B1dzSXRZ+Hbjjn/b1cIc2lzbnMOx/xD4V3FgOHbVtpQWg1HAqEYLMXcpvr+SfRSVb5h9PKczWRAIdrf50H5wLNfY16+X2gkSQQEhCdByLbvX5WK9L1jM/ymqqASRVgwjBrSu0j8cUYQyZlI64zZvBuIDbfmJtWMUK2QwAHAgmosopm9jhFbxU/XBgHP9TK1coiCIGBr+4cCIoR6PMm4fNLHQTBCslN8/cGdc4XIA4IfW5Tgdhsc7EHgo4nALXMc3SsWnXV0dWzbwiGluMF27cLy/y5swZTx4sfLk2GqNo+fVyrHNseHhGw==";
    public static final String API_KEY_VCB_BAOHIEM = "ca27b18e-0b1c-431c-90ec-79d742475392";
    public static final String ORG_UNIT_VCB_BAOHIEM = "62a2fa02ac8479129c984ab8";
    public static final String API_IDENTIFIER_VCB_BAOHIEM = "62a8132afc9f805e678c3bdc";

    public static final Long TTLMILLIS = 1000000L;
    public static final String VALIDATE_JWT = "validateJWT";

    //Cybersource Error Code
    public static final String TRANSACTION_SUCCESS = "100";
    public static final String MISSING_FIELD = "101";
    public static final String INVALID_FIELD = "102";
    public static final String TOKEN_MANAGEMENT_SERVICE_NOT_ENABLED = "150";
    public static final String CARD_NUMBER_INVALID = "231";

    //ECI
    public static final Map<String, String> ECI_ERROR_CODE_MAP;

    static {
        ECI_ERROR_CODE_MAP = new HashMap<String, String>() {
            {
                put("00", "Xác thực 3DS không thành công (ngân hàng phát hành thẻ không được bảo mật bằng 3DS, lỗi kỹ thuật hoặc cấu hình không đúng)");
                put("01", "Xác thực 3DS đã được thử nhưng không hoặc không thể hoàn thành; các lý do có thể là thẻ hoặc Ngân hàng phát hành của thẻ chưa tham gia 3DS, hoặc chủ thẻ hết thời gian ủy quyền. (Mastercard)");
                put("02", "Xác thực 3DS thành công; cả thẻ và Ngân hàng phát hành đều được bảo mật bằng 3DS. (Mastercard)");
                put("05", "Xác thực DS thành công; giao dịch được bảo mật bằng 3DS.(Visa," +
                        "American Express, JCB, Diners Club," +
                        "Discover, China UnionPay và Elo)");
                put("06", "xác thực đã được cố gắng nhưng không hoặc không thể hoàn thành; các lý do có thể là thẻ hoặc Ngân hàng phát hành của thẻ chưa tham gia 3DS.(Visa," +
                        "American Express, JCB, Diners Club," +
                        "Discover, China UnionPay và Elo)");
                put("07", "Xác thực 3DS không thành công (ngân hàng phát hành thẻ không được bảo mật bằng 3DS, lỗi kỹ thuật hoặc cấu hình không đúng)");
            }
        };
    }
    public static final Map<String, ApiKey3ds2> apiKey3ds2Map;

    static {
        Map<String, ApiKey3ds2> aMap = new HashMap<>();
        aMap.put("NL_CYBS_VTB", new ApiKey3ds2(API_KEY_VTB, API_IDENTIFIER_VTB, ORG_UNIT_VTB));
        aMap.put("NL_CYBS_STB", new ApiKey3ds2(API_KEY_STB, API_IDENTIFIER_STB, ORG_UNIT_STB));
        aMap.put("NL_CYBS_STB_TRAVEL", new ApiKey3ds2(API_KEY_STB_TRAVEL, API_IDENTIFIER_STB_TRAVEL, ORG_UNIT_STB_TRAVEL));
        aMap.put("NL_CYBS_STB_TIKI", new ApiKey3ds2(API_KEY_STB_TIKI, API_IDENTIFIER_STB_TIKI, ORG_UNIT_STB_TIKI));
        aMap.put("NL_CYBS_STB_BH", new ApiKey3ds2(API_KEY_STB_BH, API_IDENTIFIER_STB_BH, ORG_UNIT_STB_BH));
        aMap.put("NL_CYBS_STB_CBS", new ApiKey3ds2(API_KEY_STB_CBS, API_IDENTIFIER_STB_CBS, ORG_UNIT_STB_CBS));
        aMap.put("NL_CYBS_STB_VISA", new ApiKey3ds2(API_KEY_STB_VISA, API_IDENTIFIER_STB_VISA, ORG_UNIT_STB_VISA));
        aMap.put("NL_CYBS_STB_DVC1", new ApiKey3ds2(API_KEY_STB_DVC1, API_IDENTIFIER_STB_DVC1, ORG_UNIT_STB_DVC1));
        aMap.put("NL_CYBS_STB_DVC2", new ApiKey3ds2(API_KEY_STB_DVC2, API_IDENTIFIER_STB_DVC2, ORG_UNIT_STB_DVC2));
        aMap.put("NL_CYBS_VCB", new ApiKey3ds2(API_KEY_VCB, API_IDENTIFIER_VCB, ORG_UNIT_VCB));
        aMap.put("NL_CYBS_VCB_SGD", new ApiKey3ds2(API_KEY_VCB_SGD, API_IDENTIFIER_VCB_SGD, ORG_UNIT_VCB_SGD));
        aMap.put("NL_CYBS_VCB_TH", new ApiKey3ds2(API_KEY_VCB_TH, API_IDENTIFIER_VCB_TH, ORG_UNIT_VCB_TH));
        aMap.put("NL_CYBS_VCB_ST", new ApiKey3ds2(API_KEY_VCB_ST, API_IDENTIFIER_VCB_ST, ORG_UNIT_VCB_ST));
        aMap.put("NL_CYBS_VCB_OTHERS", new ApiKey3ds2(API_KEY_VCB_OTHERS, API_IDENTIFIER_VCB_OTHERS, ORG_UNIT_VCB_OTHERS));
        aMap.put("NL_CYBS_SCB_ALOTHERS", new ApiKey3ds2(API_KEY_SCB_ALOTHERS, API_IDENTIFIER_SCB_ALOTHERS, ORG_UNIT_SCB_ALOTHERS));
        aMap.put("NL_CYBS_SCB_VANTAI", new ApiKey3ds2(API_KEY_SCB_VANTAICONG, API_IDENTIFIER_SCB_VANTAICONG, ORG_UNIT_SCB_VANTAICONG));
        aMap.put("NL_CYBS_STB_BAOHIEM", new ApiKey3ds2(API_KEY_STB_BAOHIEM, API_IDENTIFIER_STB_BAOHIEM, ORG_UNIT_STB_BAOHIEM));
        aMap.put("NL_CYBS_VCB_BAOHIEM", new ApiKey3ds2(API_KEY_VCB_BAOHIEM, API_IDENTIFIER_VCB_BAOHIEM, ORG_UNIT_VCB_BAOHIEM));

        apiKey3ds2Map = Collections.unmodifiableMap(aMap);
    }
}
