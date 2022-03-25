<template>
<v-container>
    <v-row justify="center" style="height: 970px">
        <v-col cols="3" align-self="center">
            <!-- <v-row class="mb-10" justify="center">
                <v-img :src="require(`@/assets/SignIn_Logo.png`)" max-height="300" max-width="450"></v-img>
            </v-row> -->
            <v-form ref="form">

                <v-text-field v-model="id" :rules="rules.id" counter="25" label="ID" required ref="id"></v-text-field>
                <v-text-field v-model="password" :rules="rules.pwd" label="PASSWORD" type="password" required ref="pwd" @keyup.enter="signIn"></v-text-field>
                <v-row class="mt-10" justify="center">
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="goBack">
                        <v-icon class="pr-1">mdi-home</v-icon>
                        Home
                    </v-btn>
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="signIn">
                        <v-icon class="pr-1">mdi-login-variant</v-icon>
                        sign In
                    </v-btn>
                    <v-btn class="primary pl-2 pr-3" @click="signUp">
                        <v-icon class="pr-1">mdi-account</v-icon>
                        sign Up
                    </v-btn>
                </v-row>
                <v-row class="mt-5" justify="center">
                    <v-btn class="mr-5 primary pl-2 pr-3" @click="dialog = true">
                        <v-icon class="pr-1">mdi-magnify</v-icon>
                        Find ID
                    </v-btn>
                    <v-btn class="primary pl-2 pr-3" @click="dialog2 = true">
                        <v-icon class="pr-1">mdi-magnify</v-icon>
                        Find PW
                    </v-btn>
                </v-row>
                <v-row class="mt-5" justify="center">
                    <v-col cols="auto" class="mt-1">
                        <div id="naver_id_login"></div>
                    </v-col>
                    <v-col cols="auto">
                        <v-img class="kakao_btn" src="@/assets/kakao_login_medium.png" @click="loginWithKakao" />
                    </v-col>
                </v-row>
            </v-form>
        </v-col>
    </v-row>
    <v-dialog v-model="dialog" persistent max-width="550">
        <v-card>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <thead>
                            <tr>
                                <th class="text-h5" colspan="2">아이디 찾기</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-if="checkId != null">
                                <td> 아이디 </td>
                                <td>
                                    <v-text-field v-model="checkId" outlined hide-details="auto" dense readonly></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="checkTel" :rules="rules.tel" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <v-row justify="end">
                                        <v-col cols="auto">
                                            <v-btn class="primary" @click="findPW" v-if="checkId != null">비밀번호 찾기</v-btn>
                                            <v-btn class="primary" @click="findID" v-if="checkId == null">찾기</v-btn>
                                            <v-btn class="error ml-3" @click="reset">취소</v-btn>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog2" persistent max-width="550">
        <v-card>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <thead>
                            <tr>
                                <th class="text-h5" colspan="2">비밀번호 찾기</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td> 아이디 </td>
                                <td>
                                    <v-text-field v-model="checkId" :rules="rules.id" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 전화번호 </td>
                                <td>
                                    <v-text-field v-model="checkTel" :rules="rules.tel" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <v-row justify="end">
                                        <v-col cols="auto">
                                            <v-btn class="primary" @click="findPW">찾기</v-btn>
                                            <v-btn class="error ml-3" @click="reset">취소</v-btn>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>
        </v-card>
    </v-dialog>
    <v-dialog v-model="dialog3" persistent max-width="500">
        <v-card>
            <v-form ref="form">
                <v-simple-table>
                    <template slot="default">
                        <thead>
                            <tr>
                                <th class="text-h5" colspan="2">비밀번호 수정</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td> 비밀번호 </td>
                                <td>
                                    <v-text-field v-model="pwd1" :rules="rules.pwd1" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td> 비밀번호 확인 </td>
                                <td>
                                    <v-text-field v-model="pwd2" :rules="rules.pwd2" type="password" outlined hide-details="auto" dense required></v-text-field>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <v-row justify="end">
                                        <v-col cols="auto">
                                            <v-btn class="primary" @click="updatePassword">수정</v-btn>
                                            <v-btn class="error ml-3" @click="reset">취소</v-btn>
                                        </v-col>
                                    </v-row>
                                </td>
                            </tr>
                        </tbody>
                    </template>
                </v-simple-table>
            </v-form>
        </v-card>
    </v-dialog>
    <v-dialog v-model="alertDialog" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
// import router from '@/router/index'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            dialog: false,
            dialog2: false,
            dialog3: false,
            checkId: null,
            checkTel: null,
            pwd1: '',
            pwd2: '',
            id: '',
            password: '',
            rules: {
                id: [v => !!v || '아이디는 필수 입력사항입니다.',
                    v => /^[a-zA-Z0-9]*$/.test(v) || '아이디는 영문+숫자만 입력 가능합니다.',
                ],
                pwd: [v => !!v || '비밀번호는 필수 입력사항입니다.', ],
                pwd1: [v => !!v || '비밀번호는 필수 입력사항입니다.',
                    v => /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^*+=-]).{0,}$/.test(v) || '비밀번호는 영문 대소문자, 숫자, 특수문자로 구성돼야합니다.',
                    v => v.length >= 8 || '최소 8글자입니다.',
                    v => v.length <= 20 || '최대 20글자입니다.',
                ],
                pwd2: [v => !!v || '비밀번호확인은 필수 입력사항입니다.',
                    v => v === this.pwd1 || '비밀번호가 일치하지 않습니다.'
                ],
                tel: [v => !!v || '전화번호는 필수 입력사항입니다.',
                    v => /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/.test(v) || '전화번호는 숫자만 입력 가능합니다.',
                ],
            }
        }
    },
    methods: {
        goBack() {
            this.$router.push('/')
        },
        signIn() {
            let validate = this.$refs.form.validate();
            if (validate) {
                axios.get('/api/member/login', {
                        params: {
                            id: this.id,
                            password: this.password,
                        }
                    })
                    .then(res => {
                        let temp = res.data;
                        let user = {
                            id: temp.id,
                            name: temp.name,
                            authority: temp.authority,
                        };
                        this.Login({
                            user
                        })
                    })
                    .then(() => {
                        // console.log(this.getPath)
                        this.$router.push(this.getPath)
                    })
                    .catch(err => {
                        if (err.response.data == 'ID NOT FOUND') {
                            this.alertDialog = true;
                            this.alertType = 'warning';
                            this.alertMessage = '아이디가 존재하지 않습니다';
                            this.$refs.pwd.reset();
                            this.$refs.id.focus();
                        } else if (err.response.data == 'PASSWORD NOT MATCHED') {
                            this.alertDialog = true;
                            this.alertType = 'warning';
                            this.alertMessage = '비밀번호가 일치하지 않습니다';
                            this.$refs.pwd.reset();
                            this.$refs.pwd.focus();
                        }
                    })
            }
        },
        signUp() {
            this.$router.push('/authentication/signUp');
        },
        findID() {
            axios({
                    method: 'get',
                    url: `/api/member/find`,
                    params: {
                        tel: this.checkTel,
                    }
                })
                .then(res => {
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '인증 완료';
                    this.checkId = res.data;
                })
        },
        findPW() {
            axios({
                    method: 'get',
                    url: `/api/member/find`,
                    params: {
                        tel: this.checkTel,
                        id: this.checkId,
                    }
                })
                .then(res => {
                    if (res.data == 'yes') {
                        this.alertDialog = true;
                        this.alertType = 'success';
                        this.alertMessage = '정보가 일치합니다. 비밀번호를 수정해주세요';
                        this.dialog3 = true;
                    } else {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '해당되는 아이디가 없습니다';
                    }
                })
        },
        updatePassword() {
            let member;
            if (this.pwd1 == this.pwd2) {
                member = {
                    id: this.checkId,
                    password: this.pwd1,
                }
            }
            axios.put('/api/member/updateMember', member)
                .then(() => {
                    this.alertDialog = true;
                    this.alertType = 'success';
                    this.alertMessage = '수정 성공';
                    this.reset();
                })
                .catch(() => {
                    this.alertDialog = true;
                    this.alertType = 'error';
                    this.alertMessage = '수정 실패';
                })
        },
        reset() {
            this.dialog3 = false;
            this.dialog2 = false;
            this.dialog = false;
            this.checkTel = null;
            this.checkId = null;
        },
        loginWithKakao() {
            if (window.Kakao.isInitialized() == false) {
                window.Kakao.init('718ddcda2468585f014e650a9231e4e4');
            }
            window.Kakao.Auth.login({
                success: this.kakaoLoginCheck(),
                fail: function (error) {
                    console.log(error);
                },
            })
        },
        kakaoLoginCheck() {
            window.Kakao.API.request({
                url: '/v2/user/me',
                success: res => {
                    axios.get('/api/member/login', {
                            params: {
                                id: 'kakao' + res.id,
                                password: 'kakao' + res.id + 'rh7369#n',
                            }
                        })
                        .then(result => {
                            let temp = result.data;
                            let user = {
                                id: temp.id,
                                name: temp.name,
                                authority: temp.authority,
                            };
                            this.Login({
                                user
                            })
                        })
                        .then(() => {
                            this.$router.push(this.getPath)
                        })
                        .catch(err => {
                            if (err.response.data == 'ID NOT FOUND') {
                                this.$router.push({
                                    name: "Social",
                                    params: {
                                        kakao: res,
                                    }
                                });
                            }
                        })
                },
                fail: error => {
                    this.$router.push("/authentication/signIn");
                    console.log(error);
                }
            })
        },
        naverLogin(token) {
            axios.get(`/api/member/getNaverLogin/${token}`)
                .then(res => {
                    axios.get('/api/member/login', {
                            params: {
                                id: 'naver' + res.data.response.id,
                                password: 'naver' + res.data.response.id + 'rh7369#n',
                            }
                        })
                        .then(res => {
                            let temp = res.data;
                            let user = {
                                id: temp.id,
                                name: temp.name,
                                authority: temp.authority,
                            };
                            this.Login({
                                user
                            })
                        })
                        .then(() => {
                            // console.log(this.getPath)
                            this.$router.push(this.getPath)
                        })
                        .catch(err => {
                            if (err.response.data == 'ID NOT FOUND') {
                                this.$router.push({
                                    name: "Social",
                                    params: {
                                        naver: res.data.response
                                    }
                                });
                            }
                        })
                })
        },
        ...LoginStore.mapActions(['Login']),
        ...LoginStore.mapMutations(['setPath']),
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
        ...LoginStore.mapGetters(['getPath'])
    },
    mounted() {
        this.$vuetify.goTo(0);
        if (this.$route.params.nextPage != null)
            this.setPath(`${this.$route.params.nextPage}`)
        else
            this.setPath('/')

        const naver_id_login = new window.naver_id_login("8wGbrmNxZFLB0_BomNmr", "http://49.50.160.212:8085/authentication/signIn/naver");
        let link = document.location.href;
        if (link.indexOf('access_token') != -1) {
            this.naverLogin(naver_id_login.getAccessToken());
        } else {
            const state = naver_id_login.getUniqState();
            naver_id_login.setButton("white", 2, 40); // 버튼 설정
            naver_id_login.setState(state);
            // naver_id_login.setPopup(); // popup 설정을 위한 코드
            naver_id_login.init_naver_id_login();
        }
    }
}
</script>

<style>

</style>
