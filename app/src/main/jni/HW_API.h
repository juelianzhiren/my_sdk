/*****************************************************************\
*  �����ӿ�������
*   HWRC_SetWorkSpace ����ʶ����Ĺ����ռ䣬�����Ĭ��ʶ�������
*  
*   HWRC_Recognize    ʶ��
*  
*   HWRC_GetResult    ȡʶ����
\*****************************************************************/
#define HANWANG_INTEGRATED_DICT              0//1 �ֵ伯�ɡ� 0 �ֵ����

#define MAX_POINT_NUM           (2048)       //������������ 2048
#define MAX_CANDATION            (10)

#define SEGMENT_SENTENCE_REC_RAM_SIZE    (936 * 1024) //�зָ��Լ���ʶ����Ҫ��ʶ��ռ��С

//����ֵ˵��
#define HWERR_SUCCESS                   0		// �ɹ�
#define HWERR_INVALID_PARAMETER         (-1)	// ��������
#define HWERR_NOT_ENOUGH_MEMORY         (-2)	// �ڴ治��
#define HWERR_INVALID_DATA              (-3)	// ��Ч���ֵ�����
#define HWERR_INVALID_LANGUAGE			   (-5)	// ���Բ�֧��
#define HWERR_NOVALID_RESULT            (-20)   // û����Ч��ѡ
#define HWERR_INVALID_REC_HANDLE        (-21)   // �����Handle��Ч.
#define HWERR_POINTER_NOT_4BYTES_ALGN   (-22)   // �����ָ��û�����ֽڶ���
#define HWERR_INVALID_RANGE             (-23)   // ʶ��Χ��Ч
#define HWERR_INVALID_MODE              (-24)   // ʶ��ģʽ����
#define HWERR_OVER_ROWNUM               (-25)   //�ָ�������� 
#define HWERR_OUTDATE                   (-100)  //outdate

//Define error type
#define HWERR_PY_INVALIDDATA (-0x100)
#define HWERR_PY_STROKENUM   (-0x200) 
#define HWERR_PY_OUTLINE	 (-0x400)
#define HWERR_PY_POSITION    (-0x800)


//ʶ��ģʽ
#define HWRC_PINYIN  5    //ƴ���̾�ʶ��,�����ڵ��ֽ�ƴ��ʶ��


#ifdef __cplusplus
extern "C"
{
#endif

	//=======================================================================
	//����ʶ����Ĺ����ռ�
	//pHandle   [in]ʶ���� 128 ��DWORD 
	//pcRam     [in] ʶ��ռ䣬���ֽ���ʼ�����ݵ��ֻ����ʶ��Ҫ��������Ӧ�ռ��С
	//iRamSize  [in] ʶ��ռ��С
	//return:   
	//  HWERR_SUCCESS
	//  HWERR_INVALID_REC_HANDLE
	//  HWERR_INVALID_PARAMETER
	//  HWERR_NOT_ENOUGH_MEMORY
	 int HWRC_SetWorkSpace( unsigned long *pHandle, char *pcRam, long lRamSize );

	//===============================================================================
	//ʶ��Ĭ���ǵ���ʶ�� HWRC_SetRecogMode ���޸�Ϊ���Ķ̾��Ӣ�ĵ��ʡ�
	//dwHandle  [in]ʶ���� 128 ��DWORD 
	//pnPoint   [in]�ʼ�����-1��0)ÿ���ʻ�������(-1,-1)�����ʼ�������
	//return:   
	//  ������ֵ˵��
	 int HWRC_Recognize( unsigned long * pHandle, short *pnPoint );

	//===============================================================================
	//ȡ��ʶ���ѡ������ʶ���̾䡢����)
	//iMaxCandNum  [in]����ѡ����
	//pResult      [out] Ĭ��UCS2���롣ÿ����ѡ��0��β�����к�ѡ��������0��β��
	//              
	//             �磺
	//             Ӣ��дһ�����ʣ��������������ѡ�� good god
	//                          0x0067 0x006F 0x006F 0x0064 0x0000 
	//                          0x0067 0x006F 0x0064 0x0000
	//                          0x0000 //ȫ����ѡ����
	//
	//             Ӣ����д���������ʣ�ÿ��������������ѡ good god�� Yes yea
	//             ���������ص�һ�����ʵĺ�ѡ��
	//                      0x0067 0x006F 0x006F 0x0064 0x0000    good
	//                      0x0067 0x006F 0x0064 0x0000           god
	//                      0x0000
	//                      ����HWRC_GetNextBlockResult�õ��¸����ʵĺ�ѡ�����Ϊ0����ȫ����ѡ�����ϡ�û����һ�������ˡ�
	//                      0x0059 0x0065 0x0073 0x0000           yes
	//                      0x0059 0x0065 0x0061 0x0000           yea
	//                      0x0000
	//
	//return: ���pResult != NULL , 
	//            ����>=0:   ʵ�ʺ�ѡ����. 0��ѡ����Ϊ 0.
	//            HWERR_INVALID_REC_HANDLE ʶ������Ч
	//            HWERR_INVALID_PARAMETER
	//            HWERR_INVALID_RESULT û����Ч�ĺ�ѡ
	int HWRC_GetResult( unsigned long *pHandle, int iMaxCandNum, char *pResult );


	//����ʶ��ʽ������ʶ��\����ʶ��\Ӣ��ʶ��
	//dwHandle  [in]ʶ���� 128 ��DWORD 
	//iType     [in] ʶ��ʽ��
	//                HWRC_CHS_SINGLE, ����ʶ��
	//                HWRC_CHS_SENTENCE���̾�ʶ��
	//                HWRC_LATIN_WORD ����ʶ��
	//return:   ������ֵ˵��
	//      HWERR_SUCCESS
	//      HWERR_INVALID_MODE ʶ��ģʽ����
	//����ֵ, HWERR_INVALID_REC_HANDLE hHandle����
 int HWRC_SetRecogMode( unsigned long *pHandle, int iType );

	//======================================================
	//�ⲿʶ���ֵ�Ǽ�������£�����ʶ���ֵ䡣
	//�����ڲ��������ֵ䡣
	//pbDic [in]ʶ���ֵ䡣�����Ǵ����ֽڶ���ĵ�ַ��ʼ��ŵġ�
	//return:   ������ֵ˵��
	//      HWERR_SUCCESS
	//      HWERR_INVALID_REC_HANDLE
	//      HWERR_INVALID_PARAMETER
	//      HWERR_POINTER_NOT_4BYTES_ALGN
 int HWRC_SetRecogDic( unsigned long *pHandle, const unsigned char *pbDic );


	//======================================================
	//����ʶ��Χ. �粻���ô˺����������ֵ��е�Ĭ�����÷�Χʶ��
	//����Ĭ��ʶ��Χ������һ������ Ӣ��Ĭ��ʶ��ģʽ���ñ�����+Ӣ�ĵ��ʡ�
	//
	//������ dwRange == 0, ������ΪĬ��ʶ��Χ��
	//���÷�ΧҪע��ʶ���ģʽ��������õķ�Χ����ʶ��ģʽ�ķ�Χ�У�������ʧ�ܡ�
	//��:ʶ��ģʽ���������HWRC_LATIN_WORD����������ʶ��Χ ALC_PUNC_RARE ��������ʧ�ܡ�
	//return:  
	//      HWERR_SUCCESS
	//      HWERR_INVALID_REC_HANDLE
	//      HWERR_INVALID_RANGE
 int HWRC_SetRecogRange( unsigned long *pHandle, unsigned long dwRange );


#ifdef __cplusplus
}
#endif
