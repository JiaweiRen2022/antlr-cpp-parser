# Antlr Cpp Parser 

Description: A static analysis tool for C++ repository based on ANTLR.

Stage: Upgraded to support partial C++17 syntax.

Email: `renjw02@foxmail.com`

## 1. Environment
* JDK 11
* Maven
* antlr4 4.11.1

## 2. Usage Guidelines
1. Configure the repo to be resolved in `config.properties`
2. run `/entry/MainEntry` whith default config


## 3. Result 

single source cpp file parsed result as follows:

```
entry.MainEntry.navigateFile(MainEntry.java:59): ==> excute file : llvm-project\mlir\lib\Dialect\Linalg\TransformOps\LinalgTransformOps.cpp  
excuteFile(MainEntry.java:72): ======================= Summary: =================================  
excuteFile(MainEntry.java:73): File : llvm-project\mlir\lib\Dialect\Linalg\TransformOps\LinalgTransformOps.cpp  
excuteFile(MainEntry.java:74): Parse time 17   
summaryResults(MainEntry.java:84): ======================= Global =======================  
summaryResults(MainEntry.java:87): #transform::TileToForallOp::build  
summaryResults(MainEntry.java:88): params: (OpBuilder &builder,OperationState &result,Value target,ArrayRef<OpFoldResult> mixedNumThreads,ArrayAttr mapping)  
summaryResults(MainEntry.java:89): -----------------------  
summaryResults(MainEntry.java:87): #transform::TileOp::build  
summaryResults(MainEntry.java:88): params: (OpBuilder &builder,OperationState &result,TypeRange loopTypes,Value target,ArrayRef<OpFoldResult> mixedTileSizes,ArrayRef<int64_t> interchange)  
summaryResults(MainEntry.java:87): #transform::LowerPackOp::applyToOne  
summaryResults(MainEntry.java:88): params: (tensor::PackOp target,transform::ApplyToEachResultList &transformResults,transform::TransformState &state)  
summaryResults(MainEntry.java:89): -----------------------  
......
summaryResults(MainEntry.java:87): #transform::LowerUnPackOp::applyToOne  
summaryResults(MainEntry.java:88): params: (tensor::UnPackOp target,transform::ApplyToEachResultList &transformResults,transform::TransformState &state)  
summaryResults(MainEntry.java:89): -----------------------  
summaryResults(MainEntry.java:92): ======================= Classes =======================  
summaryResults(MainEntry.java:95): TClass:VectorizationPattern  
summaryResults(MainEntry.java:98): # TFunction: VectorizationPattern.matchAndRewrite(Operation *op,PatternRewriter &rewriter)  
summaryResults(MainEntry.java:98): # TFunction: VectorizationPattern(MLIRContext *context,bool vectorizeExtract)  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:95): TClass:TrivialPatternRewriter  
summaryResults(MainEntry.java:98): # TFunction: TrivialPatternRewriter(MLIRContext *context)  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:95): TClass:LinalgTransformDialectExtension  
summaryResults(MainEntry.java:98): # TFunction: LinalgTransformDialectExtension.init()  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:95): TClass:LowerPackResult  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:95): TClass:PackingMetadata  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:95): TClass:LowerUnPackOpResult  
summaryResults(MainEntry.java:100): -----------------------  
summaryResults(MainEntry.java:109): FINISHED  
```

## 4. Further Development 

1. You can configure the `log_Node_Info=true` Print all parsing nodes to handle the parts you are interested in ;
2. You can extend `LogicalNodeListenerImp` to your own needs to obtain more detailed node information ; 

