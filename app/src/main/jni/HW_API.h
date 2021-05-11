/*****************************************************************\
*  基本接口三个：
*   HWRC_SetWorkSpace 加载识别核心工作空间，并填充默认识别配置项。
*  
*   HWRC_Recognize    识别
*  
*   HWRC_GetResult    取识别结果
\*****************************************************************/
#define HANWANG_INTEGRATED_DICT              0//1 字典集成。 0 字典分离

#define MAX_POINT_NUM           (2048)       //最多输入点数： 2048
#define MAX_CANDATION            (10)

#define SEGMENT_SENTENCE_REC_RAM_SIZE    (936 * 1024) //行分割以及行识别需要的识别空间大小

//返回值说明
#define HWERR_SUCCESS                   0		// 成功
#define HWERR_INVALID_PARAMETER         (-1)	// 参数错误
#define HWERR_NOT_ENOUGH_MEMORY         (-2)	// 内存不足
#define HWERR_INVALID_DATA              (-3)	// 无效的字典数据
#define HWERR_INVALID_LANGUAGE			   (-5)	// 语言不支持
#define HWERR_NOVALID_RESULT            (-20)   // 没有有效候选
#define HWERR_INVALID_REC_HANDLE        (-21)   // 输入的Handle无效.
#define HWERR_POINTER_NOT_4BYTES_ALGN   (-22)   // 输入的指针没有四字节对齐
#define HWERR_INVALID_RANGE             (-23)   // 识别范围无效
#define HWERR_INVALID_MODE              (-24)   // 识别模式错误
#define HWERR_OVER_ROWNUM               (-25)   //分割块数超出 
#define HWERR_OUTDATE                   (-100)  //outdate

//Define error type
#define HWERR_PY_INVALIDDATA (-0x100)
#define HWERR_PY_STROKENUM   (-0x200) 
#define HWERR_PY_OUTLINE	 (-0x400)
#define HWERR_PY_POSITION    (-0x800)


//识别模式
#define HWRC_PINYIN  5    //拼音短句识别,仅限于单字节拼音识别


#ifdef __cplusplus
extern "C"
{
#endif

	//=======================================================================
	//加载识别核心工作空间
	//pHandle   [in]识别句柄 128 个DWORD 
	//pcRam     [in] 识别空间，四字节起始。根据单字或多字识别要求，申请相应空间大小
	//iRamSize  [in] 识别空间大小
	//return:   
	//  HWERR_SUCCESS
	//  HWERR_INVALID_REC_HANDLE
	//  HWERR_INVALID_PARAMETER
	//  HWERR_NOT_ENOUGH_MEMORY
	 int HWRC_SetWorkSpace( unsigned long *pHandle, char *pcRam, long lRamSize );

	//===============================================================================
	//识别。默认是单字识别。 HWRC_SetRecogMode 可修改为中文短句或英文单词。
	//dwHandle  [in]识别句柄 128 个DWORD 
	//pnPoint   [in]笔迹。（-1，0)每个笔画结束，(-1,-1)整个笔迹结束。
	//return:   
	//  见返回值说明
	 int HWRC_Recognize( unsigned long * pHandle, short *pnPoint );

	//===============================================================================
	//取得识别候选（单字识别或短句、单词)
	//iMaxCandNum  [in]最大候选个数
	//pResult      [out] 默认UCS2编码。每个候选以0结尾。所有候选结束后，有0结尾。
	//              
	//             如：
	//             英文写一个单词，如果返回两个候选串 good god
	//                          0x0067 0x006F 0x006F 0x0064 0x0000 
	//                          0x0067 0x006F 0x0064 0x0000
	//                          0x0000 //全部候选结束
	//
	//             英文书写了两个单词，每个单词有两个候选 good god， Yes yea
	//             本函数返回第一个单词的候选，
	//                      0x0067 0x006F 0x006F 0x0064 0x0000    good
	//                      0x0067 0x006F 0x0064 0x0000           god
	//                      0x0000
	//                      调用HWRC_GetNextBlockResult得到下个单词的候选。如果为0，则全部候选输出完毕。没有下一个单词了。
	//                      0x0059 0x0065 0x0073 0x0000           yes
	//                      0x0059 0x0065 0x0061 0x0000           yea
	//                      0x0000
	//
	//return: 如果pResult != NULL , 
	//            返回>=0:   实际候选个数. 0候选个数为 0.
	//            HWERR_INVALID_REC_HANDLE 识别句柄无效
	//            HWERR_INVALID_PARAMETER
	//            HWERR_INVALID_RESULT 没有有效的候选
	int HWRC_GetResult( unsigned long *pHandle, int iMaxCandNum, char *pResult );


	//设置识别方式：单字识别\多字识别\英文识别。
	//dwHandle  [in]识别句柄 128 个DWORD 
	//iType     [in] 识别方式，
	//                HWRC_CHS_SINGLE, 单字识别
	//                HWRC_CHS_SENTENCE，短句识别
	//                HWRC_LATIN_WORD 单词识别
	//return:   见返回值说明
	//      HWERR_SUCCESS
	//      HWERR_INVALID_MODE 识别模式错误
	//返回值, HWERR_INVALID_REC_HANDLE hHandle错误。
 int HWRC_SetRecogMode( unsigned long *pHandle, int iType );

	//======================================================
	//外部识别字典非集成情况下，设置识别字典。
	//引擎内部来解析字典。
	//pbDic [in]识别字典。必须是从四字节对齐的地址开始存放的。
	//return:   见返回值说明
	//      HWERR_SUCCESS
	//      HWERR_INVALID_REC_HANDLE
	//      HWERR_INVALID_PARAMETER
	//      HWERR_POINTER_NOT_4BYTES_ALGN
 int HWRC_SetRecogDic( unsigned long *pHandle, const unsigned char *pbDic );


	//======================================================
	//设置识别范围. 如不调用此函数，按照字典中的默认设置范围识别。
	//中文默认识别范围：简体一二级， 英文默认识别模式常用标点符号+英文单词。
	//
	//如输入 dwRange == 0, 则设置为默认识别范围。
	//设置范围要注意识别的模式，如果设置的范围不在识别模式的范围中，则设置失败。
	//如:识别模式如果设置了HWRC_LATIN_WORD，则若设置识别范围 ALC_PUNC_RARE ，则设置失败。
	//return:  
	//      HWERR_SUCCESS
	//      HWERR_INVALID_REC_HANDLE
	//      HWERR_INVALID_RANGE
 int HWRC_SetRecogRange( unsigned long *pHandle, unsigned long dwRange );


#ifdef __cplusplus
}
#endif
