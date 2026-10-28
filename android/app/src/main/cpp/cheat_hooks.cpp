#include <jni.h>
#include <android/log.h>
#include <unistd.h>
#include <thread>
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "MW_CHEATS", __VA_ARGS__)
extern "C" {
bool g_neverBusted=true, g_instantCooldown=true, g_noCops=false;
JNIEXPORT void JNICALL Java_com_nfsmw_androidevolved_CheatOverlayService_updateCheats(JNIEnv* e, jclass c, jboolean nb, jboolean ic, jboolean nc){
g_neverBusted=nb; g_instantCooldown=ic; g_noCops=nc; LOGI("Cheats NB=%d IC=%d NC=%d",nb,ic,nc);
}
bool MW_Pursuit_CanBust(){ if(g_neverBusted||g_noCops) return false; return true; }
void MW_Pursuit_SetCooldown(float* t){ if(t && (g_instantCooldown||g_noCops)) *t=0.0f; }
bool MW_Pursuit_ShouldSpawnCop(){ if(g_noCops) return false; return true; }
}
__attribute__((constructor)) void Init(){ LOGI("MW Cheats Injected!"); }
