package com.despegar.indec.hangar.external.sm.model

case class ClusterInfo(clusterName: String,
                        instances: List[Instance])


case class Instance(name: String,
                     address: String)
