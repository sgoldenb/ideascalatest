import scalikejdbc._

case class Member(id: Long, groupId: Long, name: String)
case class Group(id: Long, name: String, members: Seq[Member] = Nil)

object Group extends SQLSyntaxSupport[Group] {
  override val tableName = "groups"
  def apply(g: SyntaxProvider[Group])(rs: WrappedResultSet): Group = apply(g.resultName)(rs)
  def apply(g: ResultName[Group])(rs: WrappedResultSet): Group = new Group(rs.get(g.id), rs.get(g.name))
}

object Member extends SQLSyntaxSupport[Member] {
  override val tableName = "members"
  def apply(m: SyntaxProvider[Member])(rs: WrappedResultSet): Member = apply(m.resultName)(rs)
  def apply(m: ResultName[Member])(rs: WrappedResultSet): Member =
    new Member(rs.get(m.id), rs.get(m.groupId), rs.get(m.name))

  def opt(m: SyntaxProvider[Member])(rs: WrappedResultSet): Option[Member] =
    rs.longOpt(m.resultName.id).map(_ => Member(m)(rs))
}

class SomeClass {

  def someFunc = DB readOnly  { implicit session =>

   val (g, m) = (Group.syntax, Member.syntax)

  val groups: Seq[Group] =
    withSQL { select.from(Group as g).leftJoin(Member as m).on(g.id, m.groupId) }
      .one(Group(g))
      .toMany(Member.opt(m))
      .map { (group, members) => group.copy(members = members) }
      .list
      .apply()

  }

}
